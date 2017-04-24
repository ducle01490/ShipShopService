package com.shipper.model;

public class User {
	public static final int s_nonPhone = 0;
	public static final int s_nonVerified = 1;
	public static final int s_activated = 2;
	public static final int s_deactivated = 3;
	
	public static final int role_shop = 1;
	public static final int role_shipper = 2;
	
	
	private String userName;
	private String phoneNumber;
	private String password;
	private int status;
	private int role;
	
	public User(String userName, String password, int role) {
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.status = s_nonPhone;
		this.phoneNumber = "";
	}

	public User() {
		super();
	}

	public User(String userName, String phoneNumber, String password,
			int status, int role) {
		super();
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.status = status;
		this.setRole(role);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

}
