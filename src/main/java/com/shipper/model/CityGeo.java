package com.shipper.model;

import java.sql.Timestamp;

import org.json.JSONObject;

public class CityGeo {
	
	private int id;
	private String city;
	private String province;
	private String detail;
	private Timestamp updated;
	
	
	
	
	public CityGeo() {
		super();
	}
	public CityGeo(int id, String city, String province, String detail,
			Timestamp updated) {
		super();
		this.id = id;
		this.city = city;
		this.province = province;
		this.detail = detail;
		this.updated = updated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("id", this.getId());
		o.put("city", this.getCity());
		o.put("province", this.getProvince());
		
		o.put("detail", this.getDetail());
		//o.put("updated", this.getUpdated().toString());
		
		return o;
	}

}
