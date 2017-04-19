package com.shipper.model;

public class OrderTransaction {
	public static final int wait = 0;
	public static final int bidded = 1;
	public static final int shipped = 2;
	public static final int received = 3;
	public static final int fnished = 4;
	public static final int cancelled = 5;
	public static final int error = 6;
	
	private long orderId;
	private int orderStatus;
	private long shipperId;
	private String shipperName;
	
	private long startTime;
	private long finishTime;
	
	private int statusShopConfirmed;
	private int statusShipperConfirmed;
	
	
	public OrderTransaction() {
		super();
	}
	
	
	
	public OrderTransaction(long orderId, int orderStatus, long shipperId,
			String shipperName, long startTime, long finishTime,
			int statusShopConfirmed, int statusShipperConfirmed) {
		super();
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.shipperId = shipperId;
		this.shipperName = shipperName;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.statusShopConfirmed = statusShopConfirmed;
		this.statusShipperConfirmed = statusShipperConfirmed;
	}



	public OrderTransaction(long orderId, int orderStatus, long shipperId,
			String shipperName, long startTime, long finishTime) {
		super();
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.shipperId = shipperId;
		this.shipperName = shipperName;
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public long getShipperId() {
		return shipperId;
	}
	public void setShipperId(long shipperId) {
		this.shipperId = shipperId;
	}
	public String getShipperName() {
		return shipperName;
	}
	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	public int getStatusShopConfirmed() {
		return statusShopConfirmed;
	}
	public void setStatusShopConfirmed(int statusShopConfirmed) {
		this.statusShopConfirmed = statusShopConfirmed;
	}
	public int getStatusShipperConfirmed() {
		return statusShipperConfirmed;
	}
	public void setStatusShipperConfirmed(int statusShipperConfirmed) {
		this.statusShipperConfirmed = statusShipperConfirmed;
	}
	
	

}
