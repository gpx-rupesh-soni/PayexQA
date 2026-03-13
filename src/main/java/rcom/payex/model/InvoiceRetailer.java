package rcom.payex.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RETAILER_MASTER")
public class InvoiceRetailer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PAYEX_RETAILER_CODE")
	private String payex_RETAILER_CODE;	

	@Column(name = "SHOP_NAME")
	private String shop_NAME;

	@Column(name = "KEY_PERSON")
	private String key_PERSON;

	@Column(name = "MOBILE_NUMBER")
	private String mobile_NUMBER;	

	@Column(name = "EMAIL_ID")
	private String email_ID;	

	@Column(name = "ACTIVE_FLAG")
	private boolean active_FLAG;

	public String getPayex_RETAILER_CODE() {
		return payex_RETAILER_CODE;
	}

	public void setPayex_RETAILER_CODE(String payex_RETAILER_CODE) {
		this.payex_RETAILER_CODE = payex_RETAILER_CODE;
	}

	public String getShop_NAME() {
		return shop_NAME;
	}

	public void setShop_NAME(String shop_NAME) {
		this.shop_NAME = shop_NAME;
	}

	public String getKey_PERSON() {
		return key_PERSON;
	}

	public void setKey_PERSON(String key_PERSON) {
		this.key_PERSON = key_PERSON;
	}

	public String getMobile_NUMBER() {
		return mobile_NUMBER;
	}

	public void setMobile_NUMBER(String mobile_NUMBER) {
		this.mobile_NUMBER = mobile_NUMBER;
	}

	public String getEmail_ID() {
		return email_ID;
	}

	public void setEmail_ID(String email_ID) {
		this.email_ID = email_ID;
	}

	public boolean isActive_FLAG() {
		return active_FLAG;
	}

	public void setActive_FLAG(boolean active_FLAG) {
		this.active_FLAG = active_FLAG;
	}

}
