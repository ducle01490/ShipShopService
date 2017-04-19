package com.shipper.model;

public class OrderFull {
	
	private OrderInfo info;
	private OrderTransaction transaction;
	
	
	
	
	public OrderFull() {
		super();
	}
	public OrderFull(OrderInfo info, OrderTransaction transaction) {
		super();
		this.info = info;
		this.transaction = transaction;
	}
	public OrderInfo getInfo() {
		return info;
	}
	public void setInfo(OrderInfo info) {
		this.info = info;
	}
	public OrderTransaction getTransaction() {
		return transaction;
	}
	public void setTransaction(OrderTransaction transaction) {
		this.transaction = transaction;
	}
	
	
	

}
