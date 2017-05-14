package com.shipper.model;

import org.json.JSONObject;

public class Shop extends User{

	private int shopId;
	
	private String shopName;
	private String address;
	private String facebook;
	private String zalo;
	
	private String bankInfo;
	
	private String city;
	private String province;
	private int cityGeoId;
	
	
	public Shop(String userName, String password) {
		super(userName, password, User.role_shop);
		this.shopId = -1;
		this.shopName = "";
		this.address = "";
		this.facebook = "";
		this.zalo = "";
		this.bankInfo = "";
	}

	public Shop() {
		super();
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shopId", this.getShopId());
		o.put("userName", this.getUserName());
		o.put("shopName", this.getShopName());
		
		o.put("phoneNumber", this.getPhoneNumber());
		o.put("status", this.getStatus());
		o.put("address", this.getAddress());
		o.put("bankInfo", this.getBankInfo());
		o.put("facebook", this.getFacebook());
		o.put("zalo", this.getZalo());
		
		o.put("city", this.getCity());
		o.put("province", this.getProvince());
		o.put("cityGeoId", this.getCityGeoId());
		
		return o;
	}
	
	

	public Shop(String shopName, String address, String facebook,
			String zalo, String bankInfo) {
		super();
		this.shopName = shopName;
		this.address = address;
		this.facebook = facebook;
		this.zalo = zalo;
		this.setBankInfo(bankInfo);
	}

	
	
	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getZalo() {
		return zalo;
	}

	public void setZalo(String zalo) {
		this.zalo = zalo;
	}
	public String getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getCityGeoId() {
		return cityGeoId;
	}

	public void setCityGeoId(int cityGeoId) {
		this.cityGeoId = cityGeoId;
	}
	
	
	
}
