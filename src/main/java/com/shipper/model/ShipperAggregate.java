package com.shipper.model;

import org.json.JSONObject;

public class ShipperAggregate {
	
	private String shipperUsername;
	private int totalOrder;
	private int totalMoney;
	private String startTime;
	private String endTime;
	
	
	
	
	
	public ShipperAggregate(String shipperUsername, int totalOrder,
			int totalMoney, String startTime, String endTime) {
		super();
		this.shipperUsername = shipperUsername;
		this.totalOrder = totalOrder;
		this.totalMoney = totalMoney;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public String getShipperUsername() {
		return shipperUsername;
	}
	public void setShipperUsername(String shipperUsername) {
		this.shipperUsername = shipperUsername;
	}
	public int getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	public int getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shipperUserName", this.getShipperUsername());
		o.put("totalOrder", this.getTotalOrder());
		o.put("totalMoney", this.getTotalMoney());
		o.put("startTime", this.getStartTime());
		o.put("endTime", this.getEndTime());
		
		return o;
	}
	

}
