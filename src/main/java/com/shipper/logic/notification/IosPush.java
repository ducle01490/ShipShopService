package com.shipper.logic.notification;

import java.util.*;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.shipper.logic.order.OrderPush;
import com.shipper.model.User;

public class IosPush {
	public static boolean dev_or_product = false;
	
	

	public static String dev_cert_shop = "Shop_Dev_Only_Certificates.p12";//"Shop_Dev_Certificates.p12";
	public static String dev_pass_shop = "123456789";
	
	public static String pro_cert_shop = "Shop_Dis_Certificates.p12";
	public static String pro_pass_shop = "123456789";
	
	
	public static String dev_cert_ship = "Shipper_Dev_Certificates.p12";//"ship_new_certificates.p12"; //"Shipper_dev_Certificates.p12";//
	public static String dev_pass_ship = "123456789";
	
	public static String pro_cert_ship = "Shipper_Dis_Certificates.p12";//"ship_new_certificates.p12";

	public static String pro_pass_ship = "123456789";
	
	
	public static void main(String[] args) {
		samplePush();
	}
	
	public static void samplePush() {
		List<String> tempRegis = new ArrayList<String>();

//		tempRegis.add("bb6f1b34de36857a969ca1e61d111013a1e0f53c6aecfafaa4e0c02388d44eff");
//		sendPushList(initService(User.role_shipper), tempRegis, "title shipper", OrderPush.message(111, 1));
		
		
		tempRegis.add("e70ca4909df1b72fc14a5bca6612abd8e7d9a987e025e4dadaf9ea2bbd3179c3");
		sendPushList(initService(User.role_shop), tempRegis, "title shop", OrderPush.message(111, 1));
		
//		sendPushList(initService(User.role_shop), 
//				Arrays.asList("465bc05d24382c851d42b34765ab075e1f01eba5a162fa004680c5419ecb6b58"), 
//				"title shop", OrderPush.message(111, 1));
		
//		sendPushList(initService(User.role_shipper), 
//				Arrays.asList("651ca8f60c54bd1fdc2ab5956ef4318816272649bf51e9f7ab094a9bcf1c859b"), 
//				"title", OrderPush.message(111, 1));

	}
	
	
	
	public static String pushIos(int role, List<String> deviceToken, String title, String data) {
		List<String> tokens = deviceToken;
		String notification = notification(title, data);
		return pushIos(tokens, notification, role);
	}
	
	public static String pushIos(int role, String deviceToken, String title, String data) {
		List<String> tokens = new ArrayList<String>();
		//tokens.add("1");
		tokens.add(deviceToken);
		String notification = notification(title, data);
		return pushIos(tokens, notification, role);
	}
	
	
	public static String notification(String title, String message) {
		String result = "{\"aps\":{\"alert\":\""+title+"\","
				
				+ "\"badge\":0,\"sound\":\"default\"}, "
				+ "\"message\":"+message+""
				+ "}";
		System.out.println(result);
		return result;
	}
	
	public static String pushIos(List<String> deviceToken, String notification, int role) {
		pushIos(deviceToken, notification, role, false);
		pushIos(deviceToken, notification, role, true);
		return "ok";
	}
	
	public static String pushIos(List<String> deviceToken, String notification, int role, boolean devOrProduct) {
		String result = "";
		ApnsService service = initService(role, devOrProduct);
		service.push(deviceToken, notification);
		Map<String, Date> inactiveDevice = service.getInactiveDevices();
		for(String device : inactiveDevice.keySet()){
			System.out.println(device);
			result += device + "\t";
		}
		
		service.stop();
		
		return result;
	}


	public static ApnsService initService(int role, boolean devOrProduct) {
		if(role == User.role_shop) {
			if(devOrProduct) {
				System.out.println("init dev + role_shop");
				ApnsService service = APNS
						.newService()

						.withCert(IosPush.class.getClassLoader().getResourceAsStream(dev_cert_shop), dev_pass_shop)
						//.withCert("Certificates_Shop_dev.p12","123456789")

						//.withProductionDestination()
						.withSandboxDestination().build();
				
				return service;
			} else {
				ApnsService service = APNS
						.newService()
						.withCert(IosPush.class.getClassLoader().getResourceAsStream(pro_cert_shop), pro_pass_shop)
						//.withCert("doc/Certificates_Shop_dev.p12","123456789")
						.withProductionDestination()
						//.withSandboxDestination()
						.build();
				
				return service;
			}
		
		} else {
			if(devOrProduct) {
				System.out.println("init dev + role_ship");
				ApnsService service = APNS
						.newService()
						.withCert(IosPush.class.getClassLoader().getResourceAsStream(dev_cert_ship), dev_pass_ship)
						//.withCert("Certificates_Shop_dev.p12","123456789")
						//.withProductionDestination()
						.withSandboxDestination().build();
				
				return service;
			} else {
				ApnsService service = APNS
						.newService()
						.withCert(IosPush.class.getClassLoader().getResourceAsStream(pro_cert_ship), pro_pass_ship)
						//.withCert("doc/Certificates_Shop_dev.p12","123456789")
						.withProductionDestination()
						//.withSandboxDestination()
						.build();
				
				return service;
			}
		}
	}
	
	public static ApnsService initService(int role) {
		if(role == User.role_shop) {
			if(dev_or_product) {
				System.out.println("init dev + role_shop");
				ApnsService service = APNS
						.newService()

						.withCert(IosPush.class.getClassLoader().getResourceAsStream(dev_cert_shop), dev_pass_shop)
						//.withCert("Certificates_Shop_dev.p12","123456789")

						//.withProductionDestination()
						.withSandboxDestination().build();
				
				return service;
			} else {
				System.out.println("init pro + role_shop");
				ApnsService service = APNS
						.newService()
						.withCert(IosPush.class.getClassLoader().getResourceAsStream(pro_cert_shop), pro_pass_shop)
						//.withCert("doc/Certificates_Shop_dev.p12","123456789")
						.withProductionDestination()
						//.withSandboxDestination()
						.build();
				
				return service;
			}
		
		} else {
			if(dev_or_product) {
				System.out.println("init dev + role_ship");
				ApnsService service = APNS
						.newService()
						.withCert(IosPush.class.getClassLoader().getResourceAsStream(dev_cert_ship), dev_pass_ship)
						//.withCert("Certificates_Shop_dev.p12","123456789")
						//.withProductionDestination()
						.withSandboxDestination().build();
				
				return service;
			} else {
				System.out.println("init pro + role_ship");
				ApnsService service = APNS
						.newService()
						.withCert(IosPush.class.getClassLoader().getResourceAsStream(pro_cert_ship), pro_pass_ship)
						//.withCert("doc/Certificates_Shop_dev.p12","123456789")
						.withProductionDestination()
						//.withSandboxDestination()
						.build();
				
				return service;
			}
		}
		
		
		
	}

	
	public static List<String> sendPushList(ApnsService apnsService, List<String> tempRegis,String title, String message){
		List<String> returnVl = new ArrayList<String>();
		apnsService.start();
		String body = "{\"aps\":{\"alert\":\""+title+"\","
				+"\"message\":"+message+","
				//+"\"message\":\""+"hihi"+"\","
				+ "\"badge\":1,\"sound\":\"default\"}}";
		System.out.println(body);
		apnsService.push(tempRegis,
				body);
		Map<String, Date> listInActive = apnsService.getInactiveDevices();
		for(String str : listInActive.keySet()){
			returnVl.add(str.toLowerCase());	
			System.out.println(str);
		}
		
		apnsService.stop();
		return returnVl;
		
	}
	
	
}
