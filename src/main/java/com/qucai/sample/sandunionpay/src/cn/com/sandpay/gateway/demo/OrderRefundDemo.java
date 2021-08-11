package com.qucai.sample.sandunionpay.src.cn.com.sandpay.gateway.demo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import com.qucai.sample.sandunionpay.src.cn.com.sandpay.cashier.sdk.CertUtil;
import com.qucai.sample.sandunionpay.src.cn.com.sandpay.cashier.sdk.SDKConfig;

/**
 * 产品：杉德线上支付<br>
 * 交易：退货申请接口<br>
 * 日期： 2018-04<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class OrderRefundDemo {
	
	public static  Logger logger = LoggerFactory.getLogger(OrderRefundDemo.class);
	
	JSONObject header = new JSONObject();
	JSONObject body=new JSONObject();
	
	
	public void setHeader() {
		
		header.put("version", DemoBase.version);			//版本号
		header.put("method",DemoBase.ORDERREFUND);			//接口名称:退货
		header.put("productId","00000030");			 		//产品编码,详见《杉德线上支付接口规范》 附录
		header.put("mid", SDKConfig.getConfig().getMid());	//商户号
		String plMid=SDKConfig.getConfig().getPlMid();		//平台商户号
		if(plMid!=null && StringUtils.isNotEmpty(plMid)) {  //平台商户存在时接入
			header.put("accessType", "2");					//接入类型设置为平台商户接入
			header.put("plMid", plMid);
		}else {
			header.put("accessType", "1");					//接入类型设置为普通商户接入
		}		
		header.put("channelType", "07");					//渠道类型：07-互联网   08-移动端
		header.put("reqTime", DemoBase.getCurrentTime());	//请求时间		
	};
	
	
	public void setBody() {		
	
		body.put("orderCode", DemoBase.getOrderCode());		//商户订单号
		body.put("oriOrderCode", "2018050110523263");		//原商户订单号
		body.put("refundAmount", "000000000001");			//退货金额
		body.put("notifyUrl", "http://127.0.0.1/notify");	//异步通知地址
		body.put("refundReason", "退货测试");					//退货原因
		body.put("extend", "");								//扩展域	
	};
	
	
	public static void main(String[] args) throws Exception {
		
		OrderRefundDemo demo=new OrderRefundDemo();
		String reqAddr="/order/refund";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文头
		demo.setHeader();
		//设置报文体
		demo.setBody();
		
		JSONObject resp=DemoBase.requestServer(demo.header, demo.body, reqAddr);
		
		if(resp.getJSONObject("head").getString("respCode").equals("000000")) {
			logger.info("退货成功");
		}


	}
}
