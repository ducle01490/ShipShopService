package com.shipper.model;

public class OrderInfo {
	
	private long orderId;
	
	private String shopName;
	private String shopId;
	
	private String receiveAddress;
	private String customerAddress;
	private String customerName;
	private String customerPhone;
	
	private int deliveryType;
	private long deliveryPrice;
	private long productPrice;
	
	private String noteTime;
	private String noteProduct;
	
	
	
	public OrderInfo() {
		super();
	}
	public OrderInfo(long orderId, String shopName, String shopId,
			String receiveAddress, String customerAddress, String customerName,
			String customerPhone, int deliveryType, long deliveryPrice,
			long productPrice, String noteTime, String noteProduct) {
		super();
		this.orderId = orderId;
		this.shopName = shopName;
		this.shopId = shopId;
		this.receiveAddress = receiveAddress;
		this.customerAddress = customerAddress;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.deliveryType = deliveryType;
		this.deliveryPrice = deliveryPrice;
		this.productPrice = productPrice;
		this.noteTime = noteTime;
		this.noteProduct = noteProduct;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public int getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(int deliveryType) {
		this.deliveryType = deliveryType;
	}
	public long getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(long deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	public long getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(long productPrice) {
		this.productPrice = productPrice;
	}
	public String getNoteTime() {
		return noteTime;
	}
	public void setNoteTime(String noteTime) {
		this.noteTime = noteTime;
	}
	public String getNoteProduct() {
		return noteProduct;
	}
	public void setNoteProduct(String noteProduct) {
		this.noteProduct = noteProduct;
	}
	
	

}
