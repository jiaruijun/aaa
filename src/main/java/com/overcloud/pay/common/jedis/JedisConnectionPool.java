package com.overcloud.pay.common.jedis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.constst.Constants;

public class JedisConnectionPool {

    // 数据源
    private static final String[] dataSources = new String[] { Constants.JEDIS_DATASOURCE };

    // 针对不同的连接产生不同的JedisPool，由于JedisPool采用的是GenericObjectPool而非KeyedObjectPool,所以得自己构造Map
    private static Map<String, JedisPool> poolMap = null;

    // 最大活跃连接数
    private static final int DEFAULT_MAXACTIVE = 200;

    // 最大保留空闲连接数
    private static final int DEFAULT_MAXIDLE = 50;

    // 最大保留空闲连接数
    private static final int DEFAULT_TIMEOUT = 30000;

    // 每次清理的连接数
    private static final int DEFAULT_NUMTESTSPEREVICTIONRUN = 50;

    // 清理线程每隔多少时间(ms)清理一次连接
    private static final int DEFAULT_TIMEBETWEENEVICTIONRUNSMIllIS = 5 * 60 * 1000;

    // 如果活跃连接数达到MAXACTIVE的时候采取的策略
    private static final byte DEFAULT_WHENEXHAUSTEDACTION = GenericObjectPool.WHEN_EXHAUSTED_GROW;

    private static JedisConnectionPool jedisUtil = null;

    public static JedisConnectionPool getInstance() {
        if (jedisUtil == null) {
            jedisUtil = new JedisConnectionPool();
            jedisUtil.init();
        }
        return jedisUtil;
    }

    private JedisConnectionPool() {
    }

    public synchronized Jedis getJedis(String dataSource) {
        if (poolMap != null && poolMap.size() != 0 && dataSource != null && !"".equals(dataSource)) {
            JedisPool pool = poolMap.get(dataSource);
            if (pool != null) {
                return pool.getResource();
            }
        }

        return null;
    }

    public void returnBrokenJedis(String dataSource, Jedis jedis) {
        if (dataSource == null || "".equals(dataSource) || jedis == null || poolMap == null || poolMap.size() == 0) {
            return;
        }
        JedisPool pool = poolMap.get(dataSource);
        if (pool != null) {
            pool.returnBrokenResource(jedis);
        }
    }

    public void returnJedis(String dataSource, Jedis jedis) {
        if (dataSource == null || "".equals(dataSource) || jedis == null || poolMap == null || poolMap.size() == 0) {
            return;
        }
        JedisPool pool = poolMap.get(dataSource);
        if (pool != null) {
            pool.returnResource(jedis);
        }
    }

    private int getInt(String value, int default_value) {
        if (value == null || "".equals(value)) {
            return default_value;
        }
        int ret = -1;
        try {
            ret = Integer.parseInt(value);
        }
        catch (Exception e) {
            ret = default_value;
        }
        return ret;
    }

    // 访问配置文件初始化poolMap
    private synchronized void init() {
        poolMap = new HashMap<String, JedisPool>();
        for (String dataSource : dataSources) {
            try {
                String host = SysConfig.getVal("redis.host");
                int port = SysConfig.getInt("redis.port", 0);
                String password = SysConfig.getVal("redis.pass");
                if (host == null || "".equals(host) || port == 0) {
                    continue;
                }
                int maxActive = this.getInt(null, DEFAULT_MAXACTIVE);
                int maxIdle = this.getInt(null, DEFAULT_MAXIDLE);
                int numTestsPerEvictionRun = this.getInt(null, DEFAULT_NUMTESTSPEREVICTIONRUN);
                int timeBetweenEvictionRunsMillis = this.getInt(null, DEFAULT_TIMEBETWEENEVICTIONRUNSMIllIS);
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxActive(maxActive);
                config.setMaxIdle(maxIdle);
                config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
                config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
                config.setWhenExhaustedAction(DEFAULT_WHENEXHAUSTEDACTION);
                // JedisPool pool = new JedisPool(config, host, port,
                // DEFAULT_TIMEOUT, password);
                JedisPool pool = new JedisPool(config, host, port, DEFAULT_TIMEOUT);
                poolMap.put(dataSource, pool);
            }
            catch (Exception e) {
                System.out.println("[plist-error] - get jedispool init param error! " + e.getMessage());
            }
        }

        if (poolMap.size() < dataSources.length) {
            System.out.println("[error] - init JedisConnectionPoolMap error map's size = " + poolMap.size()
                    + ", dataSource'length = " + dataSources.length);
        }
    }
}
