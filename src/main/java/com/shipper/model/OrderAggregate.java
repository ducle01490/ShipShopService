package com.shipper.model;

import org.json.JSONObject;

public class OrderAggregate {

	private long totalOrder;
	private long shipMoney;
	private long productMoney;
	private long paidMoney;
	
	private String date;
	
	
	public OrderAggregate() {
		super();
	}


	public OrderAggregate(long totalOrder, long shipMoney, long productMoney,
			long paidMoney, String date) {
		super();
		this.totalOrder = totalOrder;
		this.shipMoney = shipMoney;
		this.productMoney = productMoney;
		this.paidMoney = paidMoney;
		this.date = date;
	}
	
	
	public long getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(long totalOrder) {
		this.totalOrder = totalOrder;
	}
	public long getShipMoney() {
		return shipMoney;
	}
	public void setShipMoney(long shipMoney) {
		this.shipMoney = shipMoney;
	}
	public long getProductMoney() {
		return productMoney;
	}
	public void setProductMoney(long productMoney) {
		this.productMoney = productMoney;
	}
	public long getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(long paidMoney) {
		this.paidMoney = paidMoney;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("date", this.getDate());
		o.put("totalOrder", this.getTotalOrder());
		o.put("shipMoney", this.getShipMoney());
		o.put("productMoney", this.getProductMoney());
		o.put("paidMoney", this.getPaidMoney());
		
		return o;
	}
	
}
