package com.shipper.model;

public class Shipper extends User {
	private long shipperId;
	
	private String shipperName;
	private String motorNumber;
	private String birthDay;
	private String address;
	private String idNumber;
	
	private long preLoginTime;
	private String preLoginDid;
	private String preLoginGps;
	
	
	private String currentGps;
	private long updateGpsTime;
	
	
	
	
	public Shipper() {
		super();
	}
	
	public Shipper(String shipperName, String motorNumber, String birthDay,
			String address, String idNumber, long preLoginTime,
			String preLoginDid, String preLoginGps, String currentGps,
			long updateGpsTime) {
		super();
		this.shipperName = shipperName;
		this.motorNumber = motorNumber;
		this.birthDay = birthDay;
		this.address = address;
		this.idNumber = idNumber;
		this.preLoginTime = preLoginTime;
		this.preLoginDid = preLoginDid;
		this.preLoginGps = preLoginGps;
		this.currentGps = currentGps;
		this.updateGpsTime = updateGpsTime;
	}
	
	
	
	
	
	public long getShipperId() {
		return shipperId;
	}

	public void setShipperId(long shipperId) {
		this.shipperId = shipperId;
	}

	public String getShipperName() {
		return shipperName;
	}
	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}
	public String getMotorNumber() {
		return motorNumber;
	}
	public void setMotorNumber(String motorNumber) {
		this.motorNumber = motorNumber;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public long getPreLoginTime() {
		return preLoginTime;
	}
	public void setPreLoginTime(long preLoginTime) {
		this.preLoginTime = preLoginTime;
	}
	public String getPreLoginDid() {
		return preLoginDid;
	}
	public void setPreLoginDid(String preLoginDid) {
		this.preLoginDid = preLoginDid;
	}
	public String getPreLoginGps() {
		return preLoginGps;
	}
	public void setPreLoginGps(String preLoginGps) {
		this.preLoginGps = preLoginGps;
	}
	public String getCurrentGps() {
		return currentGps;
	}
	public void setCurrentGps(String currentGps) {
		this.currentGps = currentGps;
	}
	public long getUpdateGpsTime() {
		return updateGpsTime;
	}
	public void setUpdateGpsTime(long updateGpsTime) {
		this.updateGpsTime = updateGpsTime;
	}
	
	
	
	
	

}
