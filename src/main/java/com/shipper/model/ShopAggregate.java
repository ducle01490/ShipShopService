package com.shipper.model;

import org.json.JSONObject;

public class ShopAggregate {
	private String shopUserName;
	private long totalOrder;
	private long shipMoney;
	private long productMoney;
	private long paidMoney;
	private long finishOrder;
	private long cancelOrder;
	
	
	
	
	
	public ShopAggregate(String shopUserName, long totalOrder,
			long shipMoney, long productMoney, long paidMoney, long finishOrder, long cancelOrder) {
		super();
		this.setShopUserName(shopUserName);
		this.totalOrder = totalOrder;
		this.shipMoney = shipMoney;
		this.productMoney = productMoney;
		this.setPaidMoney(paidMoney);
		this.finishOrder = finishOrder;
		this.cancelOrder = cancelOrder;
	}
	
	public long getTotalOrder() {
		return totalOrder;
	}



	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shopUserName", this.getShopUserName());
		o.put("totalOrder", this.getTotalOrder());
		o.put("shipMoney", this.getShipMoney());
		o.put("productMoney", this.getProductMoney());
		o.put("paidMoney", this.getPaidMoney());
		o.put("cancelOrder", this.getCancelOrder());
		o.put("finishOrder", this.getFinishOrder());
		
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

	public long getFinishOrder() {
		return finishOrder;
	}

	public void setFinishOrder(long finishOrder) {
		this.finishOrder = finishOrder;
	}

	public long getCancelOrder() {
		return cancelOrder;
	}

	public void setCancelOrder(long cancelOrder) {
		this.cancelOrder = cancelOrder;
	}

	public void setTotalOrder(long totalOrder) {
		this.totalOrder = totalOrder;
	}
	

}
