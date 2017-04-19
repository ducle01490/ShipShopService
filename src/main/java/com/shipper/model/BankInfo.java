package com.shipper.model;

public class BankInfo {
	private String bankName;
	private String bankNumber;
	
	
	
	public BankInfo() {
		super();
	}
	public BankInfo(String bankName, String bankNumber) {
		super();
		this.bankName = bankName;
		this.bankNumber = bankNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	
	

}
