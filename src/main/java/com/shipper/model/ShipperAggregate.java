package com.shipper.model;

import org.json.JSONObject;

public class ShipperAggregate {
	
	private String shipperUsername;
	private long totalOrder;
	private long totalMoney;
	private long productMoney;
	
	
	
	
	
	public ShipperAggregate(String shipperUsername, long totalOrder,
			long totalMoney, long productMoney) {
		super();
		this.shipperUsername = shipperUsername;
		this.totalOrder = totalOrder;
		this.totalMoney = totalMoney;
		this.productMoney = productMoney;
	}
	public String getShipperUsername() {
		return shipperUsername;
	}
	public void setShipperUsername(String shipperUsername) {
		this.shipperUsername = shipperUsername;
	}
	public long getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	public long getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shipperUserName", this.getShipperUsername());
		o.put("totalOrder", this.getTotalOrder());
		o.put("totalMoney", this.getTotalMoney());
		o.put("productMoney", this.getProductMoney());
		
		return o;
	}
	public long getProductMoney() {
		return productMoney;
	}
	public void setProductMoney(long productMoney) {
		this.productMoney = productMoney;
	}
	

}
