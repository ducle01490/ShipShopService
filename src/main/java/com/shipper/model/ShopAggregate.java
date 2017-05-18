package com.shipper.model;

import org.json.JSONObject;

public class ShopAggregate {
	private String shopUserName;
	private long totalOrder;
	private long shipMoney;
	private long productMoney;
	private long paidMoney;
	
	
	
	
	
	public ShopAggregate(String shopUserName, long totalOrder,
			long shipMoney, long productMoney, long paidMoney) {
		super();
		this.setShopUserName(shopUserName);
		this.totalOrder = totalOrder;
		this.shipMoney = shipMoney;
		this.productMoney = productMoney;
		this.setPaidMoney(paidMoney);
	}
	
	public long getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}


	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shopUserName", this.getShopUserName());
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

	public String getShopUserName() {
		return shopUserName;
	}

	public void setShopUserName(String shopUserName) {
		this.shopUserName = shopUserName;
	}

	public long getPaidMoney() {
		return paidMoney;
	}

	public void setPaidMoney(long paidMoney) {
		this.paidMoney = paidMoney;
	}

	public long getShipMoney() {
		return shipMoney;
	}

	public void setShipMoney(long shipMoney) {
		this.shipMoney = shipMoney;
	}
	

}
