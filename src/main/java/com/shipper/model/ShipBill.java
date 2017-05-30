package com.shipper.model;

import java.sql.Date;

import org.json.JSONObject;

public class ShipBill {
	
	private int id;
	private String shopUserName;
	private Date billDate;
	private Date orderFromDate;
	private Date orderToDate;
	private long money;
	private String billMethod;
	private String updatedBy;
	
	
	public ShipBill() {
		super();
	}
	public ShipBill(int id, String shopUserName, Date billDate,
			Date orderFromDate, Date orderToDate, long money,
			String billMethod, String updatedBy) {
		super();
		this.id = id;
		this.shopUserName = shopUserName;
		this.billDate = billDate;
		this.orderFromDate = orderFromDate;
		this.orderToDate = orderToDate;
		this.money = money;
		this.billMethod = billMethod;
		this.updatedBy = updatedBy;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShopUserName() {
		return shopUserName;
	}
	public void setShopUserName(String shopUserName) {
		this.shopUserName = shopUserName;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Date getOrderFromDate() {
		return orderFromDate;
	}
	public void setOrderFromDate(Date orderFromDate) {
		this.orderFromDate = orderFromDate;
	}
	public Date getOrderToDate() {
		return orderToDate;
	}
	public void setOrderToDate(Date orderToDate) {
		this.orderToDate = orderToDate;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public String getBillMethod() {
		return billMethod;
	}
	public void setBillMethod(String billMethod) {
		this.billMethod = billMethod;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		
		o.put("shopUserName", this.getShopUserName());
		o.put("billDate", this.getBillDate());
		o.put("orderFromDate", this.getOrderFromDate());
		o.put("orderToDate", this.getOrderToDate());
		o.put("money",this.getMoney());
		o.put("billMethod", this.getBillMethod());
		o.put("updatedBy", this.getUpdatedBy());
		
		
		return o;
	}

}
