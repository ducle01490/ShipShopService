package com.shipper.logic.account;


import org.json.JSONObject;

public class AccountLogic {
	/*
	status: { ok / error / ...}
	data: {...}
	error: {
	    code: 401,
	    message: ....
	}
	 */


	
	public static JSONObject register(String userName, String passWord, int userType) {
		JSONObject data = new JSONObject();
		
		
		
		return data;
	}
	


	
	public static JSONObject login(String userName, String passWord, int userType) {
		JSONObject data = new JSONObject();
		
		
		
		return data;
	}
	

	
	
	public static JSONObject resetPassword(String userName, int userType) {
		JSONObject data = new JSONObject();
		
		
		
		return data;
	}
	

	
	public static JSONObject updatePassword(String userName, String newPassword, String verifiedCode, int userType) {
		JSONObject data = new JSONObject();
		
		
		
		return data;
	}
	
}
