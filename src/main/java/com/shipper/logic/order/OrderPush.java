package com.shipper.logic.order;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.shipper.dao.ShipperDAO;
import com.shipper.logic.notification.MobilePush;
import com.shipper.model.OrderInfo;
import com.shipper.model.User;

public class OrderPush {
	public static final String order_bidded = "Đơn hàng đã được nhận";
	public static final String order_shipped = "Hàng đã được chuyển";
	public static final String order_received = "Khách đã nhận hàng";
	public static final String order_cancel = "Đơn hàng đã bị hủy";
	public static final String order_finish = "Đơn hàng đã hoàn thành";
	public static final String order_update = "Đơn hàng đã sửa đổi";
	public static final String order_new = "Có đơn hàng mới";
	
	public static ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	public static void pushShipNewOrder(final int orderId) {
		executorService.execute(new Runnable() {
		    public void run() {
		    	List<String> shippers = ShipperDAO.getAllShipperUserName();
		    	for(String shipUserName: shippers) {
		    		pushUser(shipUserName, User.role_shipper, order_new, 
		    				message(orderId, OrderInfo.order_new));
		    	}
		    }
		});
	}
	
	public static void pushOrder(String shopUserName, String shipperUserName, int orderId, int orderStatus) {
		if(orderStatus == OrderInfo.order_bidded) {
			pushShopBidded(shopUserName, orderId);
		} else if(orderStatus == OrderInfo.order_shipped) {
			pushShopShipped(shopUserName, orderId);
		} else if(orderStatus == OrderInfo.order_received) {
			pushShopReceived(shopUserName, orderId);
		} else if(orderStatus == OrderInfo.order_cancel) {
			pushShipCancel(shipperUserName, orderId);
		} else if(orderStatus == OrderInfo.order_finish) {
			pushShipFinish(shipperUserName, orderId);
		} 
	}
	
	public static void pushShopUpdate(String shopUserName, int orderId) {
		pushUser(shopUserName, User.role_shop, order_update, message(orderId, OrderInfo.order_update));
	}
	public static void pushShipUpdate(String shipUserName, int orderId) {
		pushUser(shipUserName, User.role_shipper, order_update, message(orderId, OrderInfo.order_update));
	}
	
	public static void pushShopBidded(String shopUserName, int orderId) {
		pushUser(shopUserName, User.role_shop, order_bidded, message(orderId, OrderInfo.order_bidded));
	}
	
	public static void pushShopShipped(String shopUserName, int orderId) {
		pushUser(shopUserName, User.role_shop, order_shipped, message(orderId, OrderInfo.order_shipped));
	}
	
	public static void pushShopReceived(String shopUserName, int orderId) {
		pushUser(shopUserName, User.role_shop, order_received, message(orderId, OrderInfo.order_received));
	}
	
	
	
	public static void pushShipCancel(String shipperUserName, int orderId) {
		pushUser(shipperUserName, User.role_shipper, order_cancel, message(orderId, OrderInfo.order_cancel));
	}
	
	public static void pushShipFinish(String shipperUserName, int orderId) {
		pushUser(shipperUserName, User.role_shipper, order_finish, message(orderId, OrderInfo.order_finish));
	}
	
	public static String message(int orderId, int orderStatus) {
		JSONObject message = new JSONObject();
		
		message.put("orderId", orderId);
		message.put("status", orderStatus);
		
		return message.toString();
	}
	
	
	public static void pushUser(String userName, int role, String title, String message) {
		MobilePush.pushUser(role, userName, title, message);
	}
	
	
	public static void main(String[] args) {
		System.out.println(message(1234, 0));
	}

}
