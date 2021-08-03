package com.qucai.sample.sandfastpay.main.java.cn.com.sand.pay.online.sdk.util;

import java.util.Random;

public class RandomStringGenerator {
	public static String getRandomStringByLength(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
