package com.shipper.model;

import java.util.List;

public class Shop {

	private long shopId;
	
	private String shopName;
	private String address;
	private String facebook;
	private String zalo;
	
	private String bankInfo;
	
	
	

	public Shop() {
		super();
	}

	public Shop(String shopName, String address, String facebook,
			String zalo, String bankInfo) {
		super();
		this.shopName = shopName;
		this.address = address;
		this.facebook = facebook;
		this.zalo = zalo;
		this.bankInfo = bankInfo;
	}

	
	
	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
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


	
	
	
}
