package com.shipper.logic.order;

import java.util.List;

import org.json.JSONObject;

import com.shipper.dao.OrderDAO;
import com.shipper.dao.ShipperDAO;
import com.shipper.dao.ShopDAO;
import com.shipper.logic.Constant;
import com.shipper.model.OrderInfo;
import com.shipper.model.Shipper;
import com.shipper.model.Shop;

public class OrderLogic {

	public static synchronized JSONObject updateOrderStatus(int orderId) {
		JSONObject o = new JSONObject();

		return o;
	}

	public static JSONObject bidOrder(int orderId, String shipperUserName) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<Shipper> shipperList = ShipperDAO.getShipperByUserName(shipperUserName);
		boolean checkShipperNull = (shipperList.size() == 0);
		if (checkShipperNull || shipperUserName == null
				|| shipperUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "shipper not existed | shipper is null");

			result.put("error", error);

			return result;
		} else {
			List<OrderInfo> orderList = OrderDAO.getOrderInfoById(orderId);
			
			if(orderList.size() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_order_null);
				error.put("message", "order not existed");

				result.put("error", error);

				return result;
			}
			Shipper shipper = shipperList.get(0);
			OrderInfo order = orderList.get(0);
			int status = order.getOrderStatus();
			
			if (status == OrderInfo.order_wait) {
				// Update password
				boolean r = OrderDAO.bidOrder(orderId, shipper.getShipperId(), shipper.getUserName(), shipper.getShipperName());

				if (r) {
					result.put("status", Constant.status_ok);
					
					data.put("bidded", r);
					data.put("message", "success bid order");
					result.put("data", data);

					error.put("code", Constant.error_non);
					error.put("message", "no error");

					result.put("error", error);

					return result;
				} else {
					result.put("status", Constant.status_error);
					
					
					result.put("data", data);

					error.put("code", Constant.error_db);
					error.put("message", "db error");
					result.put("error", error);

					return result;
				}
			} else {
				result.put("status", Constant.status_ok);
				
				data.put("bidded", false);
				data.put("message", "fail to bid order");
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

				return result;
			}
		}

	}

	public static JSONObject cancelOrder(int orderId, String shopUserName) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<Shop> shopList = ShopDAO.getShopByUserName(shopUserName);
		boolean checkShopNull = (shopList.size() == 0);
		if (checkShopNull || shopUserName == null
				|| shopUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "shop not existed | shop is null");

			result.put("error", error);

			return result;
		} else {
			List<OrderInfo> orderList = OrderDAO.getOrderInfoById(orderId);
			
			if(orderList.size() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_order_null);
				error.put("message", "order not existed");

				result.put("error", error);

				return result;
			}
			Shop shop = shopList.get(0);
			OrderInfo order = orderList.get(0);
			int status = order.getOrderStatus();
			
			if (status == OrderInfo.order_wait && order.getShopUserName().equals(shop.getUserName())) {
				// Update password
				boolean r = OrderDAO.cancelOrder(orderId);

				if (r) {
					result.put("status", Constant.status_ok);
					
					data.put("cancel", r);
					data.put("message", "success cancel order");
					result.put("data", data);

					error.put("code", Constant.error_non);
					error.put("message", "no error");

					result.put("error", error);

					return result;
				} else {
					result.put("status", Constant.status_error);
					
					
					result.put("data", data);

					error.put("code", Constant.error_db);
					error.put("message", "db error");
					result.put("error", error);

					return result;
				}
			} else {
				result.put("status", Constant.status_ok);
				
				data.put("cancel", false);
				data.put("message", "fail to cancel order status: " + status + " -- userName: " + shop.getUserName());
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

				return result;
			}
		}
	}

	private static int id_counter = 0;
	private static boolean isInit = false;

	public static synchronized int genOrderId() {
		if (!isInit) {
			id_counter = OrderDAO.getCurrentId();
			isInit = true;
		}
		id_counter++;
		return id_counter;
	}

	public static JSONObject createOrder(String orderTitle, String userName,
			String receiveAddress, String customerAddress, String customerName,
			String customerPhone, int deliveryType, long deliveryPrice,
			long productPrice, String noteTime, String noteProduct) {

		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<Shop> shops = ShopDAO.getShopByUserName(userName);
		boolean checkShopNull = (shops.size() == 0);
		if (checkShopNull || userName == null || userName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message",
					"shop not existed | userName is null | password is null");

			result.put("error", error);

			return result;
		}
		int order_id = genOrderId();
		int shopId = shops.get(0).getShopId();
		String shopName = shops.get(0).getShopName();
		boolean r = OrderDAO.createOrder(order_id, orderTitle, shopId, userName, shopName,
				receiveAddress, customerAddress, customerName, customerPhone,
				deliveryType, deliveryPrice, productPrice, noteTime,
				noteProduct);

		if (r) {
			result.put("status", Constant.status_ok);

			data.put("orderId", order_id);
			data.put("result", "success");
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "database error");

			result.put("error", error);
		}

		return result;
	}

	public static JSONObject getOrder(int orderId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = OrderDAO.getOrderInfoById(orderId);
		boolean orderNull = orders.size() == 0;
		if (orderNull) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		} else {
			OrderInfo order = orders.get(0);
			result.put("status", Constant.status_ok);

			data.put("order", order.infoToJSON());
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}

		return result;
	}
	
	public static JSONObject getOrderFull(int orderId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = OrderDAO.getOrderInfoById(orderId);
		boolean orderNull = orders.size() == 0;
		if (orderNull) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		} else {
			OrderInfo order = orders.get(0);
			result.put("status", Constant.status_ok);

			data.put("order", order.infoToJSON());
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}

		return result;
	}

}
