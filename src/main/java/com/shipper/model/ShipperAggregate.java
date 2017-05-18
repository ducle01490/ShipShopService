package com.shipper.model;

import org.json.JSONObject;

public class ShipperAggregate {
	
	private String shipperUsername;
	private long totalOrder;
	private long shipMoney;
	private long productMoney;
	private long paidMoney;
	
	
	
	
	
	public ShipperAggregate(String shipperUsername, long totalOrder,
			long shipMoney, long productMoney, long paidMoney) {
		super();
		this.shipperUsername = shipperUsername;
		this.totalOrder = totalOrder;
		this.setShipMoney(shipMoney);
		this.productMoney = productMoney;
		this.paidMoney = paidMoney;
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


	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shipperUserName", this.getShipperUsername());
		o.put("totalOrder", this.getTotalOrder());
		o.put("shipMoney", this.getShipMoney());
		o.put("productMoney", this.getProductMoney());
		o.put("paidMoney", this.getPaidMoney());
		
		return o;
	}
	public long getProductMoney() {
		return productMoney;
	}
	public void setProductMoney(long productMoney) {
		this.productMoney = productMoney;
	}
	public long getShipMoney() {
		return shipMoney;
	}
	public void setShipMoney(long shipMoney) {
		this.shipMoney = shipMoney;
	}
	public long getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(long paidMoney) {
		this.paidMoney = paidMoney;
	}
	

}
