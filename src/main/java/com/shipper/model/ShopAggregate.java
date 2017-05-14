package com.shipper.model;

import org.json.JSONObject;

public class ShopAggregate {
	private String shopUserName;
	private long totalOrder;
	private long totalMoney;
	private long productMoney;
	private long paidMoney;
	
	
	
	
	
	public ShopAggregate(String shopUserName, long totalOrder,
			long totalMoney, long productMoney, long paidMoney) {
		super();
		this.setShopUserName(shopUserName);
		this.totalOrder = totalOrder;
		this.totalMoney = totalMoney;
		this.productMoney = productMoney;
		this.setPaidMoney(paidMoney);
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
		
		o.put("shopUserName", this.getShopUserName());
		o.put("totalOrder", this.getTotalOrder());
		o.put("totalMoney", this.getTotalMoney());
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
	

}
