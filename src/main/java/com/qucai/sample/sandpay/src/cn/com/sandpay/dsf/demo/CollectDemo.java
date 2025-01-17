/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-dsf-demo
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2018-4-25 下午4:51:35
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * pxl         2018-4-25        Initailized
 */
package com.qucai.sample.sandpay.src.cn.com.sandpay.dsf.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qucai.sample.sandpay.src.cn.com.sandpay.cashier.sdk.CertUtil;
import com.qucai.sample.sandpay.src.cn.com.sandpay.cashier.sdk.SDKConfig;

import com.alibaba.fastjson.JSONObject;

/**
 * 产品：杉德代收付产品<br>
 * 交易：代收<br>
 * 日期： 2018-04<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class CollectDemo { 
	
	public static  Logger logger = LoggerFactory.getLogger(CollectDemo.class);

	JSONObject request = new JSONObject();
	
	/** 
	* 组织请求报文         
	*/
	void setRequest() {
		
		request.put("version", DemoBase.version);                                // 版本号        
		request.put("productId", DemoBase.PRODUCTID_COLLECTION_TOC);             // 产品ID        
		request.put("tranTime", DemoBase.getCurrentTime());                      // 交易时间      
		request.put("orderCode", DemoBase.getOrderCode());                       // 订单号        
		request.put("tranAmt", "000000000012");                                  // 金额          
		request.put("currencyCode", DemoBase.CURRENCY_CODE);                     // 币种          
		request.put("accAttr", "0");                                             // 账户属性       0-对私   1-对公 
		request.put("accType", "4");                                             // 账号类型        3-公司账户  4-银行卡
		request.put("accNo", "6226220209634996");                                // 扣款账户号    
		request.put("accName", "蒙世花");                                        	 // 账户名        
		request.put("bankName", "农业银行");                                    	 // 账户开户行名称
		request.put("provNo", "350000");                                         // 开户省份编码  
		request.put("cityNo", "350200");                                         // 开户城市编码  
		request.put("certType", "0101");                                         // 开户证件类型  
		request.put("certNo", "35210119770917002X");                             // 开户证件号码  
		request.put("cardId", "35210119770917002X");                             // 身份证号      
		request.put("phone", "15721447947");                                     // 银行预留手机号
		request.put("bankInsCode", "");                                          // 银联机构号    
		request.put("purpose", "collection");                                    // 用途说明      
		request.put("clearCycle", "");                                           // 清算模式      
		request.put("channelType", "");                                          // 渠道类型      
		request.put("extendParams", "");                                         // 业务扩展参数  
		request.put("contractId", "");                                           // 协议号        
		request.put("royaltyInfo", "");                                          // 分账信息      
		request.put("riskRateInfo", "");                                         // 风控信息域    
		request.put("requestReserved", "");                                      // 请求方保留域  
		request.put("extend", "");                                               // 扩展域 
	}
	
	public static void main(String[] args,String merchantId) throws Exception {
		
		CollectDemo demo = new CollectDemo();
		String reqAddr="/collection";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc(merchantId);
		//加载证书
		CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文
		demo.setRequest();
		
		String merId = SDKConfig.getConfig().getMid(); 			//商户ID
		String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID
		
		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.COLLECTION, merId, plMid);
		
		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");	
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("处理状态：["+resp.getString("resultFlag")+"]");
		}else {
			logger.error("服务器请求异常！！！");	
		}
	}


}
