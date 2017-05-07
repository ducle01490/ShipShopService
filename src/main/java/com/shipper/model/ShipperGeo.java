package com.shipper.model;

import java.sql.Timestamp;

import org.json.JSONObject;

public class ShipperGeo {
	
	private String shipperUserName;
	private String longitude;
	private String latitude;
	private String address;
	private Timestamp updated;
	
	
	
	public ShipperGeo() {
		
	}
	
	
	public ShipperGeo(String shipperUserName, String longitude,
			String latitude, String address, Timestamp updated) {
		super();
		this.shipperUserName = shipperUserName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
		this.updated = updated;
	}
	
	public String getShipperUserName() {
		return shipperUserName;
	}
	public void setShipperUserName(String shipperUserName) {
		this.shipperUserName = shipperUserName;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shipperUserName", this.shipperUserName);
		o.put("latitude", this.latitude);
		o.put("longitude", this.longitude);
		o.put("address", this.address);
		o.put("updated", this.updated.toString());
		
		
		return o;
	}
	

}
