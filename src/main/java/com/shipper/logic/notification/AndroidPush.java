package com.shipper.logic.notification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shipper.model.User;

public class AndroidPush {

	public static String API_KEY_SHOP = "AIzaSyDsO0YYsDIPhDAFrQL0UWNrzzIBjSSPwkI";
	public static String API_KEY_SHIP = "AIzaSyDsO0YYsDIPhDAFrQL0UWNrzzIBjSSPwkI";
	
	public static String pushAndroid(List<String> deviceToken, String title, String data, int role) {
		try {
			return push(content(deviceToken, title, data), role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String pushAndroid(String deviceToken, String title, String data, int role) {
		try {
			return push(content(deviceToken, title, data), role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static Content content(List<String> deviceRegs, String title, String data) {
		Content c = new Content(deviceRegs, title, data);
		return c;
	}
	
	public static Content content(String deviceReg, String title, String data) {
		List<String> deviceRegs = new ArrayList<String>();
		deviceRegs.add(deviceReg);
		Content c = new Content(deviceRegs, title, data);
		return c;
	}
	
	
	public static String push(Content content, int role) throws Exception {
		String result = "";
		
		List<String> returnVl = new ArrayList<>();
		
		
		URL url = new URL("https://android.googleapis.com/gcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		
		if(role == User.role_shop) {
			conn.setRequestProperty("Authorization", "key=" + API_KEY_SHOP);
		} else if(role == User.role_shipper) {
			conn.setRequestProperty("Authorization", "key=" + API_KEY_SHIP);
		}
		
		conn.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(wr, content);
		
//		Gson gson = new Gson();
//        String json = gson.toJson(content);
//        wr.writeUTF(json);
		
		
		wr.flush();
		wr.close();
		
		
		int responseCode = conn.getResponseCode();
		result += responseCode;
		System.out.println(responseCode);

		if (responseCode == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject tmp = jsonArray.getJSONObject(i);
				System.out.println(tmp);
				
			}
			// System.out.println(sb.toString());
			result += sb.toString();
		}
		
		for(String item : returnVl){
			System.out.println(item);
		}
		
		return result;
	}
	

	public static class Content implements Serializable{
		public List<String> registration_ids;
		public Map<String, String> data;
		
		public Content(List<String> deviceRegs, String title, String message) {
			this.registration_ids = deviceRegs;
			this.createData(title, message);
		}
		
		public Content() {
			this.registration_ids = new ArrayList<String>();
			this.data = new HashMap<String, String>();
		}

		public void addRegId(String regId) {
			if (registration_ids == null)
				registration_ids = new ArrayList<String>();
			registration_ids.add(regId);
		}

		public void createData(String title, String message) {
			if (data == null)
				data = new HashMap<String, String>();

			data.put("title", title);
			data.put("message", message);
		}

	}
	
	
	public static void main(String[] args) {
		List<String> ds = new ArrayList<String>();
		//ds.add("eLWxmA0faE0:APA91bGKim2pR6l8jimQfN5xilL0W6T1cyHMhYLfpBxut7s8B88Acm_JlVcvUuTggPUDkzX6MZgddeDxTaM1F2ejlYKf0Iwftiy_dAaAONfgacziKrVEnOY0jiZo1TokEpHzG0d5LCBp");
		ds.add("cK0aAqde8Lc:APA91bEUGmUtZXlPT0g_Gjrk3dt6auIQJyv9Zw3ONOOOMeAHdugY5uOgZ9jY_4lNLTC5Qx8wp4ifHabxUqP-c0yv1Pr7UFVrGMczr6wVIRJJNZw7oVEUfHmt9PvpuzWF-LKUIvJMAM_t"
				//"eT9NrovTT4Y:APA91bFineC_1qGLYbkcDCfd1vgDbjM_4ujOH4I9vRnztbHWRdriP9mKFnTXOmGwzPZ0e-aDxv9Pvc0nxuK1l8rlBJxQQXal-cwNUCVpzmrcZ-gYaq924c9-bK8zaAqzKTcm7WdzwOKs"
				//"ctOv1oG5rDU:APA91bFJ6Cz-vD8kCf48_uFPFjgT58Gq08eRg0beZk24hWPwqRbvut0alQHq8AjvKy-Mh1xV0DsnqogZdvKwGPmg4bjig2MxSKTngaNXEZANKwEvDZ6LdNr0gDYhjpBGMpg17SxiMF67"
				);
		try {
			sendPushToDevices(ds, new Content(ds, "kyhoolee", "kyhoolee local"));
			push(new Content(ds, "kyhoolee", "kyhoolee local"), User.role_shipper);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	

	public static List<String> sendPushToDevices(List<String> listDeviceIds, Content content) throws Exception {
		List<String> returnVl = new ArrayList<>();
		
		
//		Content content = new Content();
//		for (String str : listDeviceIds) {
//			content.addRegId(str);
//		}
		
		
		///content.createData("Kyhoolee title", "Kyhoolee Notification Message");
		
		
		URL url = new URL("https://android.googleapis.com/gcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "key=" + API_KEY_SHOP);
		conn.setDoOutput(true);
		ObjectMapper mapper = new ObjectMapper();
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		mapper.writeValue(wr, content);
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		System.out.println(responseCode);

		if (responseCode == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject tmp = jsonArray.getJSONObject(i);
				System.out.println(tmp);
				if (tmp.has("error"))
					returnVl.add(listDeviceIds.get(i));
			}
			// System.out.println(sb.toString());
		}
		
		for(String item : returnVl){
			System.out.println(item);
		}
		return returnVl;
	}

}
