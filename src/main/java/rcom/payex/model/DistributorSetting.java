package rcom.payex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="DISTRIBUTOR_SETTINGS")
public class DistributorSetting {

	@Id
	@Column(name="DISTRIBUTOR_SETTING_ID")
	Integer DISTRIBUTOR_SETTING_ID;
	
	@Column(name="PAYEX_DISTRIBUTOR_CODE")
	String PAYEX_DISTRIBUTOR_CODE;
	
	@Column(name="PAYMENT_CREDIT_ALLOWED")
	Integer PAYMENT_CREDIT_ALLOWED;
	
	@Column(name="MIN_AMOUNT_FOR_CREDIT")
	Integer MIN_AMOUNT_FOR_CREDIT;
	
	@Column(name="BLOCKED_RETAILERS_FOR_CREDIT")
	String BLOCKED_RETAILERS_FOR_CREDIT;
	
	@Column(name="PAYMENT_CREDIT_IN_DAYS")
	Integer PAYMENT_CREDIT_IN_DAYS;
		
	@Column(name="INVOICE_SETTINGS")
	String INVOICE_SETTINGS;
	
	//@author RahulB 09-06-2021 Sany payment integration process 2
	@Column(name="ORDER_SETTINGS")
	String ORDER_SETTINGS;
	
	@Column(name="DN_SETTINGS")
	String DN_SETTINGS;
	
	@Column(name="AUTO_SCHEDULE")
	Integer AUTO_SCHEDULE;
	
	@Column(name="PAYEX_INVNO_SETTINGS")
	private String PAYEX_INVNO_SETTINGS;	

	@Column(name = "BANK_UTR_SETTINGS")
	private String bankUtrSettings;
	
	@Column(name="PROCESS_INVOICE_FILE")
	int PROCESS_INVOICE_FILE;
	
	@Column(name="OUTSTANDING_PRG")
	int OUTSTANDING_PRG;
	
	@Column(name="AUTO_FAILED_UPDATE")
	int AUTO_FAILED_UPDATE;
	
	@Column(name="ACK_PROCESS_FLAG")
	private Integer ACK_PROCESS_FLAG;
	
	@Column(name="PAIDACK_PROCESS_FLAG")
	private Integer PAIDACK_PROCESS_FLAG;
	
	@Column(name = "POC_SETUP")
	private int pocSetup;
	
	@Column(name = "HIGH_AMT_BAL_UPDATE")
	private int highAmountBalUpdate;
	
	public int getAUTO_FAILED_UPDATE() {
		return AUTO_FAILED_UPDATE;
	}
	public void setAUTO_FAILED_UPDATE(int aUTO_FAILED_UPDATE) {
		AUTO_FAILED_UPDATE = aUTO_FAILED_UPDATE;
	}
	public int getOUTSTANDING_PRG() {
		return OUTSTANDING_PRG;
	}
	public void setOUTSTANDING_PRG(int oUTSTANDING_PRG) {
		OUTSTANDING_PRG = oUTSTANDING_PRG;
	}
	public int getPROCESS_INVOICE_FILE() {
		return PROCESS_INVOICE_FILE;
	}
	public void setPROCESS_INVOICE_FILE(int pROCESS_INVOICE_FILE) {
		PROCESS_INVOICE_FILE = pROCESS_INVOICE_FILE;
	}
	/**
	 * @return the aUTO_SCHEDULE
	 */
	public Integer getAUTO_SCHEDULE() {
		return AUTO_SCHEDULE;
	}
	/**
	 * @param aUTO_SCHEDULE the aUTO_SCHEDULE to set
	 */
	public void setAUTO_SCHEDULE(Integer aUTO_SCHEDULE) {
		AUTO_SCHEDULE = aUTO_SCHEDULE;
	}
	/**
	 * @return the aUTO_SCHEDULE_DAYS
	 */
	public String getAUTO_SCHEDULE_DAYS() {
		return AUTO_SCHEDULE_DAYS;
	}
	/**
	 * @param aUTO_SCHEDULE_DAYS the aUTO_SCHEDULE_DAYS to set
	 */
	public void setAUTO_SCHEDULE_DAYS(String aUTO_SCHEDULE_DAYS) {
		AUTO_SCHEDULE_DAYS = aUTO_SCHEDULE_DAYS;
	}
	/**
	 * @return the aUTO_PULL_SETTINGS
	 */
	public String getAUTO_PULL_SETTINGS() {
		return AUTO_PULL_SETTINGS;
	}
	/**
	 * @param aUTO_PULL_SETTINGS the aUTO_PULL_SETTINGS to set
	 */
	public void setAUTO_PULL_SETTINGS(String aUTO_PULL_SETTINGS) {
		AUTO_PULL_SETTINGS = aUTO_PULL_SETTINGS;
	}
	@Column(name="AUTO_SCHEDULE_DAYS")
	String AUTO_SCHEDULE_DAYS;
	
	@Column(name="AUTO_PULL_SETTINGS")
	String AUTO_PULL_SETTINGS;		
		
	@Column(name="CN_VIEW_TYPE")
	Integer CN_VIEW_TYPE;
	
	@Column(name="DN_VIEW_TYPE")
	Integer DN_VIEW_TYPE;
	
	@Column(name="IN_VIEW_TYPE")
	Integer IN_VIEW_TYPE;
			
	public Integer getCN_VIEW_TYPE() {
		return CN_VIEW_TYPE;
	}
	public void setCN_VIEW_TYPE(Integer cN_VIEW_TYPE) {
		CN_VIEW_TYPE = cN_VIEW_TYPE;
	}
	public Integer getDN_VIEW_TYPE() {
		return DN_VIEW_TYPE;
	}
	public void setDN_VIEW_TYPE(Integer dN_VIEW_TYPE) {
		DN_VIEW_TYPE = dN_VIEW_TYPE;
	}
	public Integer getIN_VIEW_TYPE() {
		return IN_VIEW_TYPE;
	}
	public void setIN_VIEW_TYPE(Integer iN_VIEW_TYPE) {
		IN_VIEW_TYPE = iN_VIEW_TYPE;
	}
	public String getDN_SETTINGS() {
		return DN_SETTINGS;
	}
	public void setDN_SETTINGS(String dN_SETTINGS) {
		DN_SETTINGS = dN_SETTINGS;
	}
	public String getINVOICE_SETTINGS() {
		return INVOICE_SETTINGS;
	}
	public void setINVOICE_SETTINGS(String iNVOICE_SETTINGS) {
		INVOICE_SETTINGS = iNVOICE_SETTINGS;
	}	
	public Integer getDISTRIBUTOR_SETTING_ID() {
		return DISTRIBUTOR_SETTING_ID;
	}
	public void setDISTRIBUTOR_SETTING_ID(Integer dISTRIBUTOR_SETTING_ID) {
		DISTRIBUTOR_SETTING_ID = dISTRIBUTOR_SETTING_ID;
	}
	public String getPAYEX_DISTRIBUTOR_CODE() {
		return PAYEX_DISTRIBUTOR_CODE;
	}
	public void setPAYEX_DISTRIBUTOR_CODE(String pAYEX_DISTRIBUTOR_CODE) {
		PAYEX_DISTRIBUTOR_CODE = pAYEX_DISTRIBUTOR_CODE;
	}
	public Integer getPAYMENT_CREDIT_ALLOWED() {
		return PAYMENT_CREDIT_ALLOWED;
	}
	public void setPAYMENT_CREDIT_ALLOWED(Integer pAYMENT_CREDIT_ALLOWED) {
		PAYMENT_CREDIT_ALLOWED = pAYMENT_CREDIT_ALLOWED;
	}
	public Integer getMIN_AMOUNT_FOR_CREDIT() {
		return MIN_AMOUNT_FOR_CREDIT;
	}
	public void setMIN_AMOUNT_FOR_CREDIT(Integer mIN_AMOUNT_FOR_CREDIT) {
		MIN_AMOUNT_FOR_CREDIT = mIN_AMOUNT_FOR_CREDIT;
	}
	public String getBLOCKED_RETAILERS_FOR_CREDIT() {
		return BLOCKED_RETAILERS_FOR_CREDIT;
	}
	public void setBLOCKED_RETAILERS_FOR_CREDIT(String bLOCKED_RETAILERS_FOR_CREDIT) {
		BLOCKED_RETAILERS_FOR_CREDIT = bLOCKED_RETAILERS_FOR_CREDIT;
	}
	public Integer getPAYMENT_CREDIT_IN_DAYS() {
		return PAYMENT_CREDIT_IN_DAYS;
	}
	public void setPAYMENT_CREDIT_IN_DAYS(Integer pAYMENT_CREDIT_IN_DAYS) {
		PAYMENT_CREDIT_IN_DAYS = pAYMENT_CREDIT_IN_DAYS;
	}	
	
	@Column(name="PAYMENT_SPLIT_ALLOWED")
	int PAYMENT_SPLIT_ALLOWED;

	/**
	 * @return the pAYMENT_SPLIT_ALLOWED
	 */
	public int getPAYMENT_SPLIT_ALLOWED() {
		return PAYMENT_SPLIT_ALLOWED;
	}
	/**
	 * @param pAYMENT_SPLIT_ALLOWED the pAYMENT_SPLIT_ALLOWED to set
	 */
	public void setPAYMENT_SPLIT_ALLOWED(int pAYMENT_SPLIT_ALLOWED) {
		PAYMENT_SPLIT_ALLOWED = pAYMENT_SPLIT_ALLOWED;
	}
	public String getPAYEX_INVNO_SETTINGS() {
		return PAYEX_INVNO_SETTINGS;
	}
	public void setPAYEX_INVNO_SETTINGS(String pAYEX_INVNO_SETTINGS) {
		PAYEX_INVNO_SETTINGS = pAYEX_INVNO_SETTINGS;
	}
	public String getBankUtrSettings() {
		return bankUtrSettings;
	}
	public void setBankUtrSettings(String bankUtrSettings) {
		this.bankUtrSettings = bankUtrSettings;
	}
	
	public Integer getACK_PROCESS_FLAG() {
		return ACK_PROCESS_FLAG;
	}
	public void setACK_PROCESS_FLAG(Integer aCK_PROCESS_FLAG) {
		ACK_PROCESS_FLAG = aCK_PROCESS_FLAG;
	}
	public int getPocSetup() {
		return pocSetup;
	}
	public void setPocSetup(int pocSetup) {
		this.pocSetup = pocSetup;
	}
	public int getHighAmountBalUpdate() {
		return highAmountBalUpdate;
	}
	public void setHighAmountBalUpdate(int highAmountBalUpdate) {
		this.highAmountBalUpdate = highAmountBalUpdate;
	}
	public String getORDER_SETTINGS() {
		return ORDER_SETTINGS;
	}
	public void setORDER_SETTINGS(String oRDER_SETTINGS) {
		ORDER_SETTINGS = oRDER_SETTINGS;
	}
	public Integer getPAIDACK_PROCESS_FLAG() {
		return PAIDACK_PROCESS_FLAG;
	}
	public void setPAIDACK_PROCESS_FLAG(Integer pAIDACK_PROCESS_FLAG) {
		PAIDACK_PROCESS_FLAG = pAIDACK_PROCESS_FLAG;
	}
	
}
