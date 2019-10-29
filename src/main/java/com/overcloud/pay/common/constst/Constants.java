package com.overcloud.pay.common.constst;

public interface Constants {

    public static String JEDIS_DATASOURCE = "redis";

    // redis数据库索引
    //游戏下载
//    public static int JEDIS_NUM_GAME_DOWNLOAD = 0;
    
    //游戏安装
    public static int JEDIS_NUM_GAME_INSTALL = 1;
    
    //游戏启动
    public static int JEDIS_NUM_GAME_STARTUP = 2;
    
    //游戏卸载
    public static int JEDIS_NUM_GAME_UNINSTALL = 3;
    
    //大厅访问数
    public static int JEDIS_NUM_GAME_VISIT = 4;
    
    //推荐位点击数
    public static int JEDIS_NUM_GAME_RECCLICK = 5;
    
//    public static int JEDIS_NUM_GAME_INTENT = 4;
//    public static int JEDIS_NUM_GAME_PAGE = 5;
//    public static int JEDIS_NUM_BOX_SWITCH = 6;
//    public static int JEDIS_NUM_PRIZE_INSIDE = 7;
//    public static int JEDIS_NUM_PRIZE_SITUATION = 8;
//    public static int JEDIS_NUM_PRIZE_SCORE = 9;
//    public static int JEDIS_NUM_USER_REG = 10;
//    public static int JEDIS_NUM_USER_LOGIN = 11;
//    public static int JEDIS_NUM_USER_REPWD = 12;

    /**
     * 游戏下载表
     */
    public static String GAME_DOWNLOAD_TABLE = "game_download";
    /**
     * 游戏安装表
     */
    public static String GAME_INSTALL_TABLE = "game_install";
    /**
     * 游戏大厅访问数表
     */
    public static String GAME_VISIT_TABLE = "game_visit";
    /**
     * 游戏卸载表
     */
    
    public static String GAME_UNINSTALL_TABLE = "game_uninstall";
    /**
     * 推荐位点击数表
     */
    public static String GAME_REC_TABLE = "user_game_rec";
    

    /**
     * 新增用户表
     */
    public static String NEW_USER_TABLE = "new_user";
    
    /**
     * 启动游戏时长表
     */
    public static String GAME_STARTUP_TABLE="game_startup";
    
    /**
     * 启动游戏次数表
     */
    public static String GAME_WHENLONG_TABLE="game_whenlong";
    




}
