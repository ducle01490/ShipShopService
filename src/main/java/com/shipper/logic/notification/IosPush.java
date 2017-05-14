package com.shipper.logic.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.shipper.model.User;

public class IosPush {
	public static boolean dev_or_product = true;
	
	
	public static String dev_cert_shop = "Certificates_Shop_dev.p12";
	public static String dev_pass_shop = "123456789";
	
	public static String pro_cert_shop = "Certificates_Shop_dev.p12";
	public static String pro_pass_shop = "123456789";
	
	
	public static String dev_cert_ship = "shipper_1_new_certificates.p12";//"ship_new_certificates.p12"; //"Shipper_dev_Certificates.p12";//
	public static String dev_pass_ship = "123456789";
	
	public static String pro_cert_ship = "shipper_1_new_certificates.p12";//"ship_new_certificates.p12";
	public static String pro_pass_ship = "123456789";
	
	
	public static void main(String[] args) {
		samplePush();
	}
	
	public static void samplePush() {
		List<String> tempRegis = new ArrayList<String>();
		tempRegis.add("1");
		tempRegis.add("5e9658a80174a038ad408e60bcff60ad1e618089bda5fd09c9905e0c004b6b38");
		sendPushList(initService(User.role_shipper), tempRegis, "title", "message");
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
		
		return result;
	}
	
	
	public static String pushIos(List<String> deviceToken, String notification, int role) {
		String result = "";
		ApnsService service = initService(role);
		service.push(deviceToken, notification);
		Map<String, Date> inactiveDevice = service.getInactiveDevices();
		for(String device : inactiveDevice.keySet()){
			System.out.println(device);
			result += device + "\t";
		}
		
		service.stop();
		
		return result;
	}


	
	
	public static ApnsService initService(int role) {
		if(role == User.role_shop) {
			if(dev_or_product) {
				
				ApnsService service = APNS
						.newService()
						.withCert(Thread.currentThread().getContextClassLoader().getResourceAsStream(dev_cert_shop), dev_pass_shop)
						//.withCert("Certificates_Shop_dev.p12","123456789")
						//.withProductionDestination()
						.withSandboxDestination().build();
				
				return service;
			} else {
				ApnsService service = APNS
						.newService()
						.withCert(Thread.currentThread().getContextClassLoader().getResourceAsStream(pro_cert_shop), pro_pass_shop)
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
						.withCert(Thread.currentThread().getContextClassLoader().getResourceAsStream(dev_cert_ship), dev_pass_ship)
						//.withCert("Certificates_Shop_dev.p12","123456789")
						//.withProductionDestination()
						.withSandboxDestination().build();
				
				return service;
			} else {
				ApnsService service = APNS
						.newService()
						.withCert(Thread.currentThread().getContextClassLoader().getResourceAsStream(pro_cert_ship), pro_pass_ship)
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
		apnsService.push(tempRegis, 
				"{\"aps\":{\"alert\":\""+title+"\","
				+"\"message\":"+message+","
				+ "\"badge\":0,\"sound\":\"default\"}}");
		Map<String, Date> listInActive = apnsService.getInactiveDevices();
		for(String str : listInActive.keySet()){
			returnVl.add(str.toLowerCase());	
			System.out.println(str);
		}
		
		apnsService.stop();
		return returnVl;
		
	}
	
	
}
