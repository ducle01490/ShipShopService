package com.shipper.logic.account;

import org.json.JSONObject;

import com.shipper.logic.HttpUtils;

public class SmsLogic {
	
	public static class Result {
		public String content;
		public boolean result;
	}
	
	public static final String sms_format = "Ma%20kich%20hoat%20tai%20khoan%20Onship%20cua%20ban%20la%20{P}";
	
	public static final String url = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?Phone=01673426521&Content=ma kich hoat 1234&ApiKey=E9A0737DBDC40ADE596A99AB8AFF85&SecretKey=69A08C9D1C6DC983D9F181F2AA74F7&IsUnicode=0&SmsType=8&Sandbox=0";
	
	public static final String url_format = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?"
			+ "Phone=%s&Content=Ma%20kich%20hoat%20tai%20khoan%20Onship%20cua%20ban%20la%20%s"
			+ "&ApiKey=E9A0737DBDC40ADE596A99AB8AFF85&SecretKey=69A08C9D1C6DC983D9F181F2AA74F7&IsUnicode=1&SmsType=8&Sandbox=0";
	
	public static final String api_key = "E20A63D9D9C90C6EB916CCEC46719A";
	public static final String secret_key = "30C65F165B7DE88D43A9F03CBD3C48";
	
	public static String getBalance() {
		String result = "";
		
		String url = "http://rest.esms.vn/MainService.svc/json/GetBalance/"
				+ api_key + "/"
				+ secret_key;
		
		System.out.println(url);
		result = HttpUtils.getRequest(url);
		
		System.out.println(result);
		
		return result;
	}
	   
	public static boolean sendSms(String phoneNumber, String verifiedCode) {
		boolean r = false;
		String full_url = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?"
				+ "Phone=" + phoneNumber + "&Content=Ma%20kich%20hoat%20tai%20khoan%20Onship%20cua%20ban%20la%20" + verifiedCode
				+ "&ApiKey=" + api_key//E9A0737DBDC40ADE596A99AB8AFF85"
				+ "&SecretKey=" + secret_key//69A08C9D1C6DC983D9F181F2AA74F7"
				+ "&IsUnicode=1"
				+ "&SmsType=8"
				+ "&Sandbox=0";
		System.out.println(full_url);
		System.out.println(full_url.charAt(96));
		String result = HttpUtils.getRequest(full_url);
		JSONObject res = new JSONObject(result);
		System.out.println(res);
		
		if(res.get("CodeResult").equals("100"))
			r = true;
		
		System.out.println(r);
		r = true;
		return r;
	}
	
	public static Result sendFullSms(String phoneNumber, String verifiedCode) {
		Result full = new Result();
		boolean r = false;
		String full_url = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?"
				+ "Phone=" + phoneNumber + "&Content=ma%20kich%20hoat%20" + verifiedCode
				+ "&ApiKey=E20A63D9D9C90C6EB916CCEC46719A"//E9A0737DBDC40ADE596A99AB8AFF85"
				+ "&SecretKey=30C65F165B7DE88D43A9F03CBD3C48"//69A08C9D1C6DC983D9F181F2AA74F7"
				+ "&IsUnicode=1"
				+ "&SmsType=8"
				+ "&Sandbox=0";
		System.out.println(full_url);
		System.out.println(full_url.charAt(96));
		String result = HttpUtils.getRequest(full_url);
		JSONObject res = new JSONObject(result);
		System.out.println(res);
		
		if(res.get("CodeResult").equals("100"))
			r = true;
		
		System.out.println(r);
		r = true;
		
		full.content = result;
		full.result = true;
		return full;
	}
	
	
	
	public static void main(String[] args) {
		sendSms("0901745999",//"01676114142",//"01673426521", 
				"1234"); //01676114142
		getBalance();
	}

}
