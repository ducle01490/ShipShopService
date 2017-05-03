package com.shipper.model;

import org.json.JSONObject;

public class OrderInfo {
	public static final int order_wait = 0;
	public static final int order_bidded = 1;
	public static final int order_shipped = 2;
	public static final int order_received = 3;
	public static final int order_finish = 4;
	public static final int order_cancel = 5;
	public static final int order_error = 6;
	
	
	private int orderId;
	private String orderTitle;
	
	private String shopUserName;
	private String shopName;
	private int shopId;
	
	private String receiveAddress;
	private String customerAddress;
	private String customerName;
	private String customerPhone;
	
	private int deliveryType;
	private long deliveryPrice;
	private long productPrice;
	
	private String noteTime;
	private String noteProduct;
	
	
	
	private int orderStatus;
	private long shipperId;
	private String shipperName;
	private String shipperUserName;
	
	private long startTime;
	private long finishTime;
	
	private int statusShopConfirmed;
	private int statusShipperConfirmed;
	
	
	private String created;
	private String updated;
	
	
	
	public OrderInfo() {
		super();
	}
	public OrderInfo(int orderId, String shopName, int shopId,
			String receiveAddress, String customerAddress, String customerName,
			String customerPhone, int deliveryType, long deliveryPrice,
			long productPrice, String noteTime, String noteProduct) {
		super();
		this.orderId = orderId;
		this.shopName = shopName;
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
	
	public JSONObject infoToJSON() {
		JSONObject o = new JSONObject();
		
		o.put("orderId", this.getOrderId());
		o.put("shopId", this.getShopId());
		o.put("shopName", this.getShopName());
		
		o.put("receiveAddress", this.getReceiveAddress());
		o.put("customerAddress", this.getCustomerAddress());
		o.put("customerName", this.getCustomerName());
		
		o.put("deliveryType", this.getDeliveryType());
		o.put("deliveryPrice", this.getDeliveryPrice());
		o.put("productPrice", this.getProductPrice());
		o.put("noteTime", this.getNoteTime());
		o.put("noteProduct", this.getNoteProduct());
		
		o.put("orderStatus", this.getOrderStatus());
		
		return o;
	}
	
	public JSONObject fullToJSON() {
		JSONObject o = new JSONObject();
		
		o.put("orderId", this.getOrderId());
		o.put("orderTitle", this.getOrderTitle());
		
		o.put("shopId", this.getShopId());
		o.put("shopUserName", this.getShopUserName());
		o.put("shopName", this.getShopName());
		
		o.put("receiveAddress", this.getReceiveAddress());
		o.put("customerAddress", this.getCustomerAddress());
		o.put("customerName", this.getCustomerName());
		o.put("customerPhone", this.getCustomerPhone());
		
		o.put("deliveryType", this.getDeliveryType());
		o.put("deliveryPrice", this.getDeliveryPrice());
		o.put("productPrice", this.getProductPrice());
		o.put("noteTime", this.getNoteTime());
		o.put("noteProduct", this.getNoteProduct());
		
		o.put("orderStatus", this.getOrderStatus());
		
		o.put("shipperId", this.getShipperId());
		o.put("shipperUserName", this.getShipperUserName());
		o.put("shipperName", this.getShipperName());
		o.put("startTime", this.getStartTime());
		o.put("finishTime", this.getFinishTime());
		o.put("statusShopConfirmed", this.getStatusShopConfirmed());
		o.put("statusShipperConfirmed", this.getStatusShipperConfirmed());
		
		o.put("created", this.getCreated());
		o.put("updated", this.getUpdated());
		
		return o;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	public String getShopUserName() {
		return shopUserName;
	}
	public void setShopUserName(String shopUserName) {
		this.shopUserName = shopUserName;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
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
	public String getShipperUserName() {
		return shipperUserName;
	}
	public void setShipperUserName(String shipperUserName) {
		this.shipperUserName = shipperUserName;
	}
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		if(orderTitle != null)
			this.orderTitle = orderTitle;
		else
			this.orderTitle = "";
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}

}
