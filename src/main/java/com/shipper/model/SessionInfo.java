package com.shipper.model;

import java.sql.Timestamp;

import org.json.JSONObject;

public class SessionInfo {
	private String userName;
	private int role;
	private Timestamp created;
	private String sessionKey;
	
	
	
	public SessionInfo() {
		super();
	}
	public SessionInfo(String userName, int role, Timestamp created, String sessionKey) {
		super();
		this.userName = userName;
		this.role = role;
		this.created = created;
		this.sessionKey = sessionKey;
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
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("sessionKey", this.getSessionKey());
		o.put("userName", this.getUserName());
		o.put("role", this.getRole());
		o.put("update", this.getCreated().toString());
		
		
		return o;
	}
	

}
