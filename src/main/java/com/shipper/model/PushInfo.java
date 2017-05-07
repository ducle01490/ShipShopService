package com.shipper.model;

import java.sql.Timestamp;

import org.json.JSONObject;

public class PushInfo {
	public static final int os_android = 0;
	public static final int os_ios = 1;
	
	
	private String userName;
	private int role;
	private int deviceOs;
	private String deviceToken;
	private Timestamp update;
	
	
	
	public PushInfo() {
		super();
	}
	public PushInfo(String userName, int role, int deviceOs,
			String deviceToken, Timestamp update) {
		super();
		this.userName = userName;
		this.role = role;
		this.deviceOs = deviceOs;
		this.deviceToken = deviceToken;
		this.update = update;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getDeviceOs() {
		return deviceOs;
	}
	public void setDeviceOs(int deviceOs) {
		this.deviceOs = deviceOs;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public Timestamp getUpdate() {
		return update;
	}
	public void setUpdate(Timestamp update) {
		this.update = update;
	}
	public static int getOsAndroid() {
		return os_android;
	}
	public static int getOsIos() {
		return os_ios;
	}
	
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("userName", this.getUserName());
		o.put("role", this.getRole());
		o.put("deviceOs", this.getDeviceOs());
		o.put("deviceToken", this.getDeviceToken());
		o.put("update", this.getUpdate().toString());
		
		
		return o;
	}
	
	

}
