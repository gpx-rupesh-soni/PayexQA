package rcom.payex.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RETAILER_MASTER")
public class Retailer {

	@Id
	@Column(name = "PAYEX_RETAILER_CODE")
	@GenericGenerator(name="PayexRetCodeGen",strategy="rcom.payex.model.PayexRetailerCodeGenerator")
	@GeneratedValue(generator="PayexRetCodeGen")
	private String payex_RETAILER_CODE;

	@Column(name = "SHOP_NAME")
	private String shop_NAME;

	@Column(name = "KEY_PERSON")
	private String key_PERSON;

	@Column(name = "MOBILE_NUMBER")
	private String mobile_NUMBER;

	@Column(name = "IMEI_NUMBER")
	private String imei_NUMBER;

	@Column(name = "EMAIL_ID")
	private String email_ID;

	@Column(name = "PIN")
	private String pin;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "CREATE_DATE")
	private Timestamp create_DATE;

	@Column(name = "UPDATE_DATE")
	private Timestamp update_DATE;
	
	@Column(name = "UPDATE_COUNT")
	private long UPDATE_COUNT;

	@Column(name = "ACTIVE_FLAG")
	private boolean active_FLAG;	

	@Column(name = "GCM_REGISTRATION_ID")
	private String gcm_REGISTRATION_ID;

	@Column(name = "OTP")
	private String otp;

	@Column(name = "OTP_TIME")
	private Long otp_TIME;
	
	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "LONGITUDE")
	private String longitude;
	
	@Column(name = "TEST_DATA")
	private int testData;
	
	@Column(name="SYNC_COUNT")
	Timestamp syncCount;	
	
	@Column(name = "GST_NUMBER")
	private String gstn;

	@Column(name="USER_TYPE")
	int userType=0;	
	
	@Column(name="PAYEX_GRAD")
	int PAYEX_GRAD=0;	
	
	@Column(name="STATE")
	String STATE;
	
	@Transient
	List<String> cpgTag;	
	
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

	public String getImei_NUMBER() {
		return imei_NUMBER;
	}

	public void setImei_NUMBER(String imei_NUMBER) {
		this.imei_NUMBER = imei_NUMBER;
	}

	public String getEmail_ID() {
		return email_ID;
	}

	public void setEmail_ID(String email_ID) {
		this.email_ID = email_ID;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getCreate_DATE() {
		return create_DATE;
	}

	public void setCreate_DATE(Timestamp create_DATE) {
		this.create_DATE = create_DATE;
	}

	public Timestamp getUpdate_DATE() {
		return update_DATE;
	}

	public void setUpdate_DATE(Timestamp update_DATE) {
		this.update_DATE = update_DATE;
	}

	public boolean isActive_FLAG() {
		return active_FLAG;
	}

	public void setActive_FLAG(boolean active_FLAG) {
		this.active_FLAG = active_FLAG;
	}

	public String getGcm_REGISTRATION_ID() {
		return gcm_REGISTRATION_ID;
	}

	public void setGcm_REGISTRATION_ID(String gcm_REGISTRATION_ID) {
		this.gcm_REGISTRATION_ID = gcm_REGISTRATION_ID;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Long getOtp_TIME() {
		return otp_TIME;
	}

	public void setOtp_TIME(Long otp_TIME) {
		this.otp_TIME = otp_TIME;
	}

	@Override
	public String toString() {		
		return payex_RETAILER_CODE;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getTestData() {
		return testData;
	}

	public void setTestData(int testData) {
		this.testData = testData;
	}

	public Timestamp getSyncCount() {
		return syncCount;
	}

	public void setSyncCount(Timestamp syncCount) {
		this.syncCount = syncCount;
	}

	public String getGstn() {
		return gstn;
	}

	public void setGstn(String gstn) {
		this.gstn = gstn;
	}

	public long getUPDATE_COUNT() {
		return UPDATE_COUNT;
	}

	public void setUPDATE_COUNT(long uPDATE_COUNT) {
		UPDATE_COUNT = uPDATE_COUNT;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getPAYEX_GRAD() {
		return PAYEX_GRAD;
	}

	public void setPAYEX_GRAD(int pAYEX_GRAD) {
		PAYEX_GRAD = pAYEX_GRAD;
	}

	public String getSTATE() {
		return STATE;
	}

	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}

	public List<String> getCpgTag() {
		return cpgTag;
	}

	public void setCpgTag(List<String> cpgTag) {
		this.cpgTag = cpgTag;
	}
	
	
	
}
