package com.shipper.logic.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.shipper.dao.OrderDAO;
import com.shipper.dao.ShipperDAO;
import com.shipper.dao.ShopDAO;
import com.shipper.logic.Constant;
import com.shipper.logic.account.AccountLogic;
import com.shipper.model.OrderInfo;
import com.shipper.model.Shipper;
import com.shipper.model.Shop;

public class OrderLogic {
	
	private static Map<Integer, Object> orderLock;
	private static boolean isInitOrderLock = false;
	
	public static synchronized void initLock() {
		if(!isInitOrderLock) {
			orderLock = new HashMap<Integer, Object>();
			isInitOrderLock = true;
		}
	}
	
	public static synchronized Object getOrderLock(int orderId) {
		initLock();
		if(orderLock.containsKey(orderId)) {
			return orderLock.get(orderId);
		} else {
			createOrderLock(orderId);
			return orderLock.get(orderId);
		}
	}
	
	public static void createOrderLock(int orderId) {
		initLock();
		orderLock.put(orderId, new Object());
	}
	
	public static synchronized void removeOrderLock(int orderId) {
		initLock();
		orderLock.remove(orderId);
	}
	
	public static JSONObject synBidOrder(int orderId, String shipperUserName) {
		JSONObject result;
		Object lock = getOrderLock(orderId);
		
		if(lock != null) {
			synchronized(lock) {
				result = bidOrder(orderId, shipperUserName);
			}
		} else {
			result = new JSONObject();
			JSONObject data = new JSONObject();
			JSONObject error = new JSONObject();
			
			result.put("status", Constant.status_ok);
			
			data.put("bidded", false);
			data.put("message", "fail to bid order");
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}
		
		return result;
	}
	
	public static JSONObject synCancelOrder(int orderId, String shopUserName) {
		JSONObject result;
		Object lock = getOrderLock(orderId);
		
		if(lock != null) {
			synchronized(lock) {
				result = cancelOrder(orderId, shopUserName);
			}
		} else {
			result = new JSONObject();
			JSONObject data = new JSONObject();
			JSONObject error = new JSONObject();
			
			result.put("status", Constant.status_ok);
			
			data.put("bidded", false);
			data.put("message", "fail to bid order");
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}
		
		return result;
	}
	
	
	

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
				// Update status
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
	private static boolean isInitCounter = false;

	public static synchronized int genOrderId() {
		if (!isInitCounter) {
			id_counter = OrderDAO.getCurrentId();
			isInitCounter = true;
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
			createOrderLock(order_id);
			
			
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
	
	public static JSONObject confirmOrderStatus(int orderId, int orderStatus, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
		//boolean r1 = OrderDAO.updateOrderStatus(orderId, orderStatus, role);
		boolean r1 = OrderDAO.confirmOrderStatus(orderId, orderStatus, role);

		if(r1) {
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	public static boolean checkUpdateStatus(int currentStatus, int updateStatus) {
		
		return true;
	}
	
	public static JSONObject updateOrderStatus(int orderId, int orderStatus, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		OrderInfo order = orders.get(0);
		int currentStatus = order.getOrderStatus();
		
		boolean r0 = checkUpdateStatus(currentStatus, orderStatus);
		if(!r0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_update_status);
			error.put("message", "can not update from current status");

			result.put("error", error);

			return result;
		}
		
		boolean r1 = OrderDAO.updateOrderStatus(orderId, orderStatus, role);
		//boolean r2 = OrderDAO.confirmOrderStatus(orderId, orderStatus, role);

		if(r1) {
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	public static JSONObject getOrderFull(int orderId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
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

			data.put("order", order.fullToJSON());
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray orderListToJSON(List<OrderInfo> orders) {
		JSONArray o = new JSONArray();
		
		for(OrderInfo order : orders) {
			o.add(orderShipShop(order));
		}
		
		return o;
	}
	
	public static JSONObject shopShort(String userName) {
		JSONObject s = new JSONObject();
		
		s.put("id", "");
		s.put("userName", "");
		s.put("phone", "");
		s.put("address", "");
		
		List<Shop> shops = ShopDAO.getShopByUserName(userName);
		if(shops.size() > 0) {
			Shop shop = shops.get(0);
			s.put("id", shop.getShopId());
			s.put("userName", shop.getShopName());
			s.put("phone", shop.getPhoneNumber());
			s.put("address", shop.getAddress());
		}
		
		return s;
	}
	
	public static JSONObject shipperShort(String userName) {
		JSONObject s = new JSONObject();
		
		s.put("id", "");
		s.put("userName", "");
		s.put("phone", "");
		s.put("address", "");
		
		List<Shipper> shippers = ShipperDAO.getShipperByUserName(userName);
		if(shippers.size() > 0) {
			Shipper ship = shippers.get(0);
			s.put("id", ship.getShipperId());
			s.put("userName", ship.getShipperName());
			s.put("phone", ship.getPhoneNumber());
			s.put("address", ship.getAddress());
		}
		
		return s;
	}
	
	public static JSONObject orderShipShop(OrderInfo order) {
		JSONObject o = order.fullToJSON();
		
		o.put("shopInfo", shopShort(order.getShopUserName()));
		o.put("shipperInfo", shipperShort(order.getShipperUserName()));
		
		return o;
	}
	
	
	public static JSONObject getOrderFullByTime(String shopUserName, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFullByUpdatedtime(startTime, endTime);
		} else {
			orders = OrderDAO.getShopOrderFullByUpdatedtime(shopUserName, startTime, endTime);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getOrderFullByTime(String shopUserName, int orderStatus, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFullByUpdatedtime(orderStatus, startTime, endTime);
		} else {
			orders = OrderDAO.getShopOrderFullByUpdatedtime(shopUserName, orderStatus, startTime, endTime);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}

	
	public static JSONObject getOrderFullList(String shopUserName, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFull(offset, numb);
		} else {
			orders = OrderDAO.getShopOrderFull(shopUserName, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getOrderFullList(String shopUserName, int orderStatus, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		List<OrderInfo> orders;
		
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFullByStatus(orderStatus, offset, numb);
		} else {
			orders = OrderDAO.getOrderFullByStatus(shopUserName, orderStatus, offset, numb);
		}
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
	
	public static JSONObject getShipperAggregate(String shipperUserName) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShipperNull(shipperUserName);
		if(checkNull || shipperUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName null");

			result.put("error", error);

			return result;
		} 
		
		
		
		result.put("status", Constant.status_ok);

		//data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
}
