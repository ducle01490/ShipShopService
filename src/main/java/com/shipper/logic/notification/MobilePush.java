package com.shipper.logic.notification;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.JSONObject;

import com.shipper.dao.PushDAO;
import com.shipper.logic.Constant;
import com.shipper.model.PushInfo;

public class MobilePush {
	
	public static JSONObject registerPush(int role, String userName, String deviceToken, int deviceOs) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		
		List<PushInfo> p = PushDAO.getPushInfoByUser(userName, role);
		if(p.size() == 0) {
			boolean r = PushDAO.createPushInfo(deviceToken, deviceOs, userName, role);
			if(r) {
				result.put("status", Constant.status_ok);
				
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
				return result;
			} else {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "pushinfo not existed");

				result.put("error", error);

				return result;
			}
		} else {
			
			// Check user exist
			
			
			boolean r = PushDAO.updatePushInfo(userName, role, deviceToken, deviceOs);
			if(r) {
				result.put("status", Constant.status_ok);
				
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
				return result;
			} else {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "pushinfo not existed");

				result.put("error", error);

				return result;
			}
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray pushJSON(List<PushInfo> ps) {
		JSONArray a = new JSONArray();
		
		for(PushInfo p: ps) {
			a.add(p.toJSON());
		}
		
		return a;
	}
	
	
	public static JSONObject getPush(int role, String userName) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		
		List<PushInfo> p = PushDAO.getPushInfoByUser(userName, role);
		if(p.size() == 0) {
			
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "pushinfo not existed");

				result.put("error", error);

				return result;
			
		} else {
			
			
				result.put("status", Constant.status_ok);
				
				data.put("push", pushJSON(p));
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
				return result;
			
		}
		
		
	}
	
	
	public static String pushAndroid(List<String> deviceTokens, String title, String message, int role) {
		return AndroidPush.pushAndroid(deviceTokens, title, message, role);
	}
	
	public static String pushIos(List<String> deviceTokens, String title, String message, int role) {
		return IosPush.pushIos(role, deviceTokens, title, message);
	}
	
	public static String pushMobile(int deviceOs, String deviceToken, String title, String message, int role) {
		if(deviceOs == PushInfo.os_android) {
			return AndroidPush.pushAndroid(deviceToken, title, message, role);
		} else if(deviceOs == PushInfo.os_ios) {
			return IosPush.pushIos(role, deviceToken, title, message);
		}
		
		return "";
	}
	
	public static JSONObject pushUser(int role, String userName, String title, String message) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		
		List<PushInfo> info = PushDAO.getPushInfoByUser(userName, role);
		
		if(info.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "pushinfo not existed");

			result.put("error", error);

			return result;
		}
		String r = "";
		for(PushInfo push : info) {
			r += pushMobile(push.getDeviceOs(), push.getDeviceToken(), title, message, role);
			r += "\t" + push.getDeviceOs() + "\t" +  push.getDeviceToken();
		}
		
		result.put("status", Constant.status_ok);
		
		data.put("result", r);
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		return result;
	}
	
	
	public static void broadcastUser(List<String> roleUserNames, String title, String message) {
		
	}
	
	
	public static void main(String[] args) {
		pushUser(2, "eeee", "kyhoolee", "kyhoolee service");
	}
	

}
