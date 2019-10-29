package com.overcloud.pay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.DBUtil;
import com.overcloud.pay.common.DateTimeUtil;
import com.overcloud.pay.common.JsonPluginsUtil;
import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.http.WebParameterUtils;
import com.overcloud.pay.common.response.ResCode;
import com.overcloud.pay.common.response.ResView;
import com.overcloud.pay.common.response.ResponseCode;
import com.overcloud.pay.common.response.ResponseView;
import com.overcloud.pay.service.OrderInfoService;
import com.overcloud.pay.vo.AuthMonthlyUserVO;
import com.overcloud.pay.vo.BodyVO;
import com.overcloud.pay.vo.BossAuthMonthlyUserVO;
import com.overcloud.pay.vo.BossIntegralPayInfoVO;
import com.overcloud.pay.vo.BossIntegralQueryReturnVO;
import com.overcloud.pay.vo.BossPayInfoVO;
import com.overcloud.pay.vo.BossQRCodePayInfoVO;
import com.overcloud.pay.vo.GsydbyOrderInfoVO;
import com.overcloud.pay.vo.MonthlyParInfoVO;
import com.overcloud.pay.vo.OrderReturnVO;
import com.overcloud.pay.vo.PayInfoVO;
import com.overcloud.pay.vo.TOkenVO;


@RestController
@RequestMapping("/gsyd")
public class OrderInfoController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    OrderInfoService service;
    
    
    /**
     * 进入大厅插入用户数据
     */
    @RequestMapping(value = "/insertUser")
    public ResView insertUser(HttpServletResponse response,HttpServletRequest request) {
    	 System.out.println("添加跨域支持");
         //添加跨域CORS
         response.addHeader("Access-Control-Max-Age", "1800");//30 min
         response.setHeader("Access-Control-Allow-Origin", "http://111.11.189.73:8013");
         response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,Content-Type,token");
         response.setHeader("Access-Control-Allow-Credentials", "true");
         response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");

    	this.logger.info("进入大厅插入用户接口");
    	 ResView vr = null;
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		String userId = params.get("userId");
    		if (userId != null && !"".equals(userId)) {
    			this.service.insertUser(params);	
    			String  price = this.service.getPrice();
    			MonthlyParInfoVO monthlyParInfoVO = this.service.getMonthlyPar();
    			PayInfoVO payInfoVO = new PayInfoVO();
    			payInfoVO.setBossPowerKey(SysConfig.getVal("bossPowerKey"));
    			payInfoVO.setBossProductId(SysConfig.getVal("bossProductId"));
    			payInfoVO.setPrice(price);
    			payInfoVO.setIsCancel(monthlyParInfoVO.getIsCancel());
    			payInfoVO.setDescribe(monthlyParInfoVO.getDescribe());
    			payInfoVO.setCustomphone(monthlyParInfoVO.getCustomphone());
    			vr = new ResView(ResCode.AJAX_SUCCESS, payInfoVO);
    		} else {

    			vr = new ResView(ResCode.AJAX_UNAME_ERR);
    		}
    		
    	 } catch (Exception e) {
 			// TODO: handle exception
 			e.printStackTrace();
 			vr = new ResView(ResCode.UNKNOW_FAIL);
 		}

    		return vr;
    }
    
    
    
    
    /**
     * 包月鉴权接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/UserAndMonthlyAuth")
    public AuthMonthlyUserVO UserAndMonthlyAuth(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端请求用户鉴权和包月鉴权接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 AuthMonthlyUserVO res = new AuthMonthlyUserVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.getAuthInfo(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    
    

    
    /**
     * 前端轮训后台 拿到支付结果
     * @param request
     * @return
     */  
    @RequestMapping(value = "/getMonthOrderInfoCode")
    public OrderReturnVO getMonthOrderInfoCode (HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端轮训后台 拿到包月支付结果接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
//    	 DBUtil.setIpParam(params, ipAddr);
    	 OrderReturnVO orderReturnVO= this.service.getMonthOrderInfoCode(params);
    	 return orderReturnVO;
    }
    
    
  //前端 查询个人包月支付信息
   	@RequestMapping("/billInfo")
   	public ResView getbill(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
   		// 获取请求参数
   		this.logger.info("前端查询个人支付记录接口");
   		Map<String, String> params = WebParameterUtils.getParameterMap(request);
   		ResView vr = null;
   		String phone = params.get("phone");
   		if (phone != null && !"".equals(phone)) {
   			// 根据用户信息查询消费记录
   			List<GsydbyOrderInfoVO> cqgdbyOrderInfoVOs = service.getGsydbyBillInfo(params);
   			vr = new ResView(ResCode.AJAX_SUCCESS, cqgdbyOrderInfoVOs);
   		} else {

   			vr = new ResView(ResCode.AJAX_UNAME_ERR);
   		}

   		return vr;
   	}
   	
   	
    /**
     * BOSS包月鉴权接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/bossMonthlyAuth")
    public BossAuthMonthlyUserVO bossMonthlyAuth(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端请求用户鉴权和包月鉴权接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 BossAuthMonthlyUserVO res = new BossAuthMonthlyUserVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.getbossAuthInfo(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    /**
     * BOSS包月新鉴权接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/bossMonthlyNewAuth")
    public BossAuthMonthlyUserVO bossMonthlyNewAuth(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端请求用户鉴权和包月新鉴权接口");
    	Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	BossAuthMonthlyUserVO res = new BossAuthMonthlyUserVO();
    	String ipAddr = RequestUtil.getIpAddr(request);
    	params.put("IP", ipAddr);
    	DBUtil.setIpParam(params, ipAddr);
    	params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	try {
    		res = this.service.getbossNewAuthInfo(params);
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	return res;
    }
    
    
    /**
     * BOSS家开请求中九正向订购退订同步接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/zte9Forwardorder")
    public BodyVO zte9Forwardorder(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("BOSS家开请求中九正向订购接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 BodyVO res = new BodyVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.zte9Forwardorder(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    
    
    /**
     * BOSS获取用户token  接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/bossGetToken")
    public TOkenVO bossGetToken(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端获取用户token接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 TOkenVO res = new TOkenVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.bossGetToken(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    
    
    /**
     * 请求Boss支付接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/bossPayInfo")
    public BossPayInfoVO bossPayInfo(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端请求BOSS支付接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 BossPayInfoVO res = new BossPayInfoVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.bossPayInfo(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    
    /**
     * 请求Boss二维码支付接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/bossQRCodePayInfo")
    public BossQRCodePayInfoVO bossQRCodePayInfo(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	this.logger.info("前端请求BOSS二维码支付接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 BossQRCodePayInfoVO res = new BossQRCodePayInfoVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.bossQRCodePayInfo(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    
    /**
     * BOSS家开请求中九二维码回调接口
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/QRReturnUrl")
    public void QRReturnUrl(HttpServletResponse response,HttpServletRequest request) throws IOException {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
//        Map<String, String>  paramss = WebParameterUtils.getParameterMap(request);
    	
    	
    	
    	 ServletInputStream servletInputStream = request.getInputStream();

         StringBuilder content = new StringBuilder();
         byte[] b = new byte[1024];
         int lens;
         while ((lens = servletInputStream.read(b)) > 0) {
             content.append(new String(b, 0, lens));
         }
         
         String strContent = content.toString();
    	
    	
    	 Map<String, String> params = new HashMap<String, String>();
    	 if(strContent!=null&&!"".equals(strContent)){
    		 params =  JsonPluginsUtil.jsonToMap(strContent);
    	 }
    	 this.logger.info("BOSS家开请求中九二维码回调接口"+strContent);
    	 
    	 BodyVO res = new BodyVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 String exception="";
    	 try {
    		this.service.zte9QRCodeReturnOrder(params);
	 	if(params.get("exception")!=null&&!"".equals(params.get("exception"))){
	 		exception= URLEncoder.encode(params.get("exception"), "UTF-8");
	 	}
    	 } catch (Exception e) {
  			// TODO: handle exception
  			e.printStackTrace();
  		}
	 	
	 	 MonthlyParInfoVO monthlyPar = service.getMonthlyPar();
    		if("".equals(params.get("productId"))||params.get("productId")==null){
    			 this.logger.info("查看重定向的URl地址是什么"+SysConfig.getVal("sendRedirect")+"?orderCode=9&exception="+exception+"&orderNo="+params.get("qrOrderId"));
//    			 response.sendRedirect(SysConfig.getVal("sendRedirect")+"?orderCode=9&exception="+exception+"&orderNo="+params.get("qrOrderId"));
    			 sendGet(SysConfig.getVal("sendRedirect")+"?orderCode=9&exception="+exception+"&orderNo="+params.get("qrOrderId"));
//    		}else if(params.get("productId").contains("158020180623000029")){
    		}else if(params.get("productId").contains(monthlyPar.getDescribe())||params.get("productId").contains(monthlyPar.getProductId())){
    			 this.logger.info("查看重定向的URl地址是什么"+SysConfig.getVal("sendRedirect")+"?orderCode=1&exception="+exception+"&orderNo="+params.get("qrOrderId"));
//    			 response.sendRedirect(SysConfig.getVal("sendRedirect")+"?orderCode=1&exception="+exception+"&orderNo="+params.get("qrOrderId"));
    			 sendGet(SysConfig.getVal("sendRedirect")+"?orderCode=1&exception="+exception+"&orderNo="+params.get("qrOrderId"));
			}
         
    		
    }
    
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}

			String urlNameString2 = connection.getURL().toString();
			if (!urlNameString.equalsIgnoreCase(urlNameString2)) {
				realUrl = new URL(urlNameString2.replace(" ", ""));
				connection = realUrl.openConnection();
			}

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
    
    /**
     * 前端轮训后台 拿到二维码支付结果
     * @param request
     * @return
     */  
    @RequestMapping(value = "/getQRReturnUrlPayStatus")
    public OrderReturnVO getQRReturnUrlPayStatus (HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	 this.logger.info("前端轮训后台 拿到二维码支付结果接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
//    	 DBUtil.setIpParam(params, ipAddr);
    	 OrderReturnVO orderReturnVO= this.service.getQRReturnUrlPayStatus(params);
    	 return orderReturnVO;
    }
    
    

    

    
    
    /**
     * 请求Boss退订接口接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/bossUnsubscribePayInfo")
    public BossPayInfoVO bossUnsubscribePayInfo(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
    	 this.logger.info("请求Boss退订接口接口");
    	 System.err.println("请求Boss退订接口接口");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 BossPayInfoVO res = new BossPayInfoVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 res = this.service.bossUnsubscribePayInfo(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return res;
    }
    
    /**
     * 包月参数管理接口
     * @param request
     * @return
     */
   @RequestMapping(value = "/getMonthlyParCategory")
    public void getMonthlyParCategory(HttpServletResponse response,HttpServletRequest request) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
   	 
   	 long start = System.currentTimeMillis();
        this.logger.info("[info]获取包月产品参数");

        ResponseView view = new ResponseView(ResponseCode.AJAX_SUCCESS);
        try {
//        	Map<String, String> params = WebParameterUtils.getParameterMap(request);
        	view.setData(this.service.list());
        }
        catch (Exception e) {
            view = new ResponseView(ResponseCode.AJAX_EXCEPTION);
            view.setMessage("[fail]获取包月产品参数, 原因:" + e.getMessage());
            this.logger.error("[fail]获取包月产品参数, 原因:" + e.getMessage(), e);
            RequestUtil.ajaxResponse(response, view);
            return;
        }

        long end = System.currentTimeMillis();
        this.logger.info("[success]获取包月产品参数, 耗时" + (end - start) + "ms");
        view.setMessage("[success]获取包月产品参数, 耗时" + (end - start) + "ms");

        RequestUtil.ajaxResponse(response, view);
   }
   
   
   /**
    * 手动请求定时查询上个月支付的数据,然后鉴权查询是否续订
    * @param request
    * @return
    */
   @RequestMapping(value = "/responseTimingOrderAuthInfo")
   public void responseTimingOrderAuthInfo(HttpServletResponse response,HttpServletRequest request) {
   	 this.logger.info("手动请求定时查询上个月支付的数据,然后鉴权查询是否续订");
   	ResponseView view = new ResponseView(ResponseCode.AJAX_SUCCESS);
   	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
   	 String ipAddr = RequestUtil.getIpAddr(request);
   	 params.put("IP", ipAddr);
   	 DBUtil.setIpParam(params, ipAddr);
   	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
   	 try {
   		this.service.timingOrderAuthInfo();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
   	 RequestUtil.ajaxResponse(response, view);
   }
   
   /**
    * 请求Boss积分查询接口
    * @param request
    * @return
    */
   @RequestMapping(value = "/bossIntegralQuery")
   public BossIntegralQueryReturnVO bossIntegralQuery(HttpServletResponse response,HttpServletRequest request) {
   	response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Headers", "Authentication");
   	this.logger.info("前端请求BOSS积分查询接口");
   	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
   	BossIntegralQueryReturnVO res = new BossIntegralQueryReturnVO();
   	 String ipAddr = RequestUtil.getIpAddr(request);
   	 params.put("IP", ipAddr);
   	 DBUtil.setIpParam(params, ipAddr);
   	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
   	 try {
   		 res = this.service.bossIntegralQuery(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       return res;
   }
    
   
   /**
    * 请求Boss积分支付接口
    * @param request
    * @return
    */
   @RequestMapping(value = "/bossIntegralPayInfo")
   public BossIntegralPayInfoVO bossIntegralPayInfo(HttpServletResponse response,HttpServletRequest request) {
   	response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Headers", "Authentication");
   	this.logger.info("前端请求BOSS积分支付接口");
   	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
   	BossIntegralPayInfoVO res = new BossIntegralPayInfoVO();
   	 String ipAddr = RequestUtil.getIpAddr(request);
   	 params.put("IP", ipAddr);
   	 DBUtil.setIpParam(params, ipAddr);
   	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
   	 try {
   		 res = this.service.bossIntegralPayInfo(params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       return res;
   }

}
