package com.overcloud.pay.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class AA {  

	public static void main(String[] args) {
  		
		String s1 = "aaabaa".replaceAll("aaa", "");
		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		System.out.println(sdf.format(d));
	}
	
	public static String getTransactionID(String pixStr, int length) {
		/*
		 * if (length<=pixStr.length()+20) { return new
		 * Random().nextInt(length)+""; }
		 */
		if (length < 10) {
			return new Random().nextInt(length) + "";
		}
		long currentTimeMillis = System.currentTimeMillis();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyMMddHHmmss"); // ʽ
																								// ȡ
		String mydate = format.format(currentTimeMillis);
		// java.util.Random();
		// String mydate = Tools.getDate("yyyyMMddHHmmss");
		int tmp = length - mydate.length();
		int pixsize = 0;
		
		if (pixStr != null) {
			pixsize = pixStr.length();
		}
		tmp = tmp - pixsize;
		String ret = "";
		String tmpStr = "0123456789";
		for (int i = 0; i < tmp; i++) {
			ret += tmpStr.charAt(((int) (Math.random() * tmpStr.length())));
		}

		return pixStr + mydate + ret;

	}

}
