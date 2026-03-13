package rcom.payex.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PAYEX_CENTRAL_INVOICES")
public class PAYEX_CENTRAL_INVOICES {
	@Column(name = "PAYEX_RETAILER_CODE")
	String PAYEX_RETAILER_CODE;
	@Column(name = "PAYEX_CPG_CODE")
	String PAYEX_CPG_CODE;
	@Column(name = "PAYEX_DISTRIBUTOR_CODE")
	String PAYEX_DISTRIBUTOR_CODE;
	
	@Column(name = "ROUTE_CODE")
	Integer ROUTE_CODE;
	@Column(name = "AGENT_CODE")
	Integer AGENT_CODE;
	@Column(name = "CPG_TYPE")
	String CPG_TYPE;
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "rcom.payex.model.PayexTransactionIdGenerator")	
	@Column(name = "PAYEX_TRANSACTION_ID")
	Integer PAYEX_TRANSACTION_ID;
	
	@Column(name = "CPG_INVOICE_NUMBER")
	String CPG_INVOICE_NUMBER;
	@Column(name = "PAYEX_INVOICE_NUMBER")
	String PAYEX_INVOICE_NUMBER;
	@Column(name = "CPG_INCVOICE_DATE")
	Date CPG_INCVOICE_DATE;
	@Column(name = "DATE_TO_DUE")
	Date DATE_TO_DUE;
	@Column(name = "CPG_INVOICE_AMOUNT")
	Double CPG_INVOICE_AMOUNT;
	@Column(name = "TOTAL_AMOUNT_DUE")
	Double TOTAL_AMOUNT_DUE;
	@Column(name = "CPG_INVOICE_CREDIT")
	int CPG_INVOICE_CREDIT;
	@Column(name = "CREATE_DATE")
	Timestamp CREATE_DATE;
	@Column(name = "UPDATE_DATE")
	Timestamp UPDATE_DATE;
	@Column(name = "DATE_TO_PRESENT")
	Timestamp DATE_TO_PRESENT;
	@Column(name = "RETAILER_ACKD_DATE_TIME")
	Timestamp RETAILER_ACKD_DATE_TIME;
	@Column(name = "DATETIME_APPROVED")
	Timestamp DATETIME_APPROVED;
	@Column(name = "DATE_TO_PAY")
	Long DATE_TO_PAY=0L;
	@Column(name = "APPROVED_BANK_UMRN")
	String APPROVED_BANK_UMRN;
	@Column(name = "APPROVED_BANK")
	String APPROVED_BANK;
	@Column(name = "RETAILER_REMARKS")
	String RETAILER_REMARKS;
	@Column(name = "APRVD_OR_PAD_BY_DA_OR_RET")
	String APRVD_OR_PAD_BY_DA_OR_RET;
	@Column(name = "RETAILER_MODE_OF_PAYMENT")
	String RETAILER_MODE_OF_PAYMENT;
	@Column(name = "ACTIVE_FLAG")
	Integer ACTIVE_FLAG=0;
	@Column(name = "PROCESS_STATUS")
	Integer PROCESS_STATUS=0;
	@Column(name = "FAILED_ON_DATE")
	Timestamp FAILED_ON_DATE;
	@Column(name = "ACH_STATUS")
	Integer ACH_STATUS=0;
	@Column(name = "RECEIVE_FROM_BANK_DATETIME")
	Timestamp RECEIVE_FROM_BANK_DATETIME;
	@Column(name = "RECEIVE_FROM_BANK_AMOUNT")
	Double RECEIVE_FROM_BANK_AMOUNT;
	@Column(name = "BANK_TRANSACTION_ID")
	String BANK_TRANSACTION_ID;
	@Column(name = "PAYER_BANK_TRANSACTION_ID")
	String PAYER_BANK_TRANSACTION_ID;
	@Column(name = "NODAL_STATUS")
	Integer NODAL_STATUS=0;
	@Column(name = "PAYEX_TO_CPG_PYMNT_RFRNCE")
	String PAYEX_TO_CPG_PYMNT_RFRNCE;
	@Column(name = "PAYEX_TO_CPG_PAYMENT_DATE")
	Timestamp PAYEX_TO_CPG_PAYMENT_DATE;
	/*@Column(name = "PAYMENT_TYPE")
	String PAYMENT_TYPE;*/
	@Column(name = "LOYALTY_POINTS")
	Integer LOYALTY_POINTS=0;
	@Column(name = "UPDATE_COUNT")
	Timestamp UPDATE_COUNT;
	@Column(name = "INVOICE_INFO")
	String INVOICE_INFO;
	@Column(name = "PAYEX_PAYMENT_ID")
	Long PAYEX_PAYMENT_ID;
	
	@Column(name = "INVOICE_INFO_EXTRA")
	String INVOICE_INFO_EXTRA="";	
	
	@Column(name = "INVOICE_SETTINGS")
	String INVOICE_SETTINGS;
	@Column(name="HAS_MAKER_CHECKER")
	Integer HAS_MAKER_CHECKER=0;
	
	@Column(name="DISTRIBUTOR_NAME")
	String DISTRIBUTOR_NAME;
	
	@Column(name="ICON")
	int ICON;
	
	@Column(name="INVOICE_UPDATE")
	Timestamp INVOICE_UPDATE;
			
	@Column(name="RETAILER_CODE")
	String RETAILER_CODE;
	
	@Column(name="RETAILER_TAG")
	String RETAILER_TAG;
	
	@Column(name="TAXABLE_AMOUNT")
	Double TAXABLE_AMOUNT;
			
	@Column(name="DIV_CODE")
	String DIV_CODE;
	
	@Column(name="INVOICE_FILE")
	int INVOICE_FILE = 0;	
	
	@Column(name="INVOICE_TYPE")
	Integer INVOICE_TYPE=0;
	
	@Column(name="VIEW_TYPE")
	Integer VIEW_TYPE;
	
	@Column(name="SEQUENCE_FLAG")
	String SEQUENCE_FLAG;
	
	@Column(name="DOC_CODE")
	String DOC_CODE;
	
	@Column(name="DOC_REF")
	String DOC_REF;

	@Column(name="FINANCIAL_YEAR")
	Integer  FINANCIAL_YEAR;
	
	@Column(name="AUTO_SCHEDULE")
	private
	int AUTO_SCHEDULE; 
	
	@Column(name="AMOUNT_APPROVED")
	double AMOUNT_APPROVED;
	
	@Column(name="PART_PAY_FLAG")
	int PART_PAY_FLAG;
	
	@Column(name="POSTING_NO")
	String POSTING_NO;
	
	@Column(name="POSTING_REASON")
	String POSTING_REASON;
		
	@Column(name="POSTING_DATE")
	Date POSTING_DATE;
	
	@Column(name="AMOUNT_BANK")
	private double AMOUNT_BANK;
	
	@Column(name = "AMOUNT_CN")
	private double AMOUNT_CN;
	
	@Column(name = "PAYMENT_MODE")
	private long PAYMENT_MODE;
	
	@Column(name = "AMOUNT_CD")
	private double AMOUNT_CD;
	
	@Column(name = "AREA_CODE")
	private String AREA_CODE;

	@Column(name = "SETTLED_DATE")
	private Timestamp SETTLED_DATE;
	
	@Column(name = "SETTLED_REVERT_DATE")
	private Timestamp SETTLED_REVERT_DATE;
	
	@Column(name = "DOC_REF_NUMBER")
	private String DOC_REF_NUMBER;
	
	@Column(name = "TCS")
	private double tcs;
	
	@Column(name = "DOC_NUMBER")
	private String DOC_NUMBER;
	
	

	public String getDOC_NUMBER() {
		return DOC_NUMBER;
	}
	public void setDOC_NUMBER(String dOC_NUMBER) {
		DOC_NUMBER = dOC_NUMBER;
	}
	public String getPOSTING_NO() {
		return POSTING_NO;
	}
	public void setPOSTING_NO(String pOSTING_NO) {
		POSTING_NO = pOSTING_NO;
	}
	public String getPOSTING_REASON() {
		return POSTING_REASON;
	}
	public void setPOSTING_REASON(String pOSTING_REASON) {
		POSTING_REASON = pOSTING_REASON;
	}
	public Date getPOSTING_DATE() {
		return POSTING_DATE;
	}
	public void setPOSTING_DATE(Date pOSTING_DATE) {
		POSTING_DATE = pOSTING_DATE;
	}
	public int getPART_PAY_FLAG() {
		return PART_PAY_FLAG;
	}
	public void setPART_PAY_FLAG(int pART_PAY_FLAG) {
		PART_PAY_FLAG = pART_PAY_FLAG;
	}
	public double getAMOUNT_APPROVED() {
		return AMOUNT_APPROVED;
	}
	public void setAMOUNT_APPROVED(double aMOUNT_APPROVED) {
		AMOUNT_APPROVED = aMOUNT_APPROVED;
	}
	public Integer getFINANCIAL_YEAR() {
		return FINANCIAL_YEAR;
	}
	public void setFINANCIAL_YEAR(Integer fINANCIAL_YEAR) {
		FINANCIAL_YEAR = fINANCIAL_YEAR;
	}
	public String getDOC_CODE() {
		return DOC_CODE;
	}
	public void setDOC_CODE(String dOC_CODE) {
		DOC_CODE = dOC_CODE;
	}
	public String getSEQUENCE_FLAG() {
		return SEQUENCE_FLAG;
	}
	public void setSEQUENCE_FLAG(String sEQUENCE_FLAG) {
		SEQUENCE_FLAG = sEQUENCE_FLAG;
	}
	public Integer getVIEW_TYPE() {
		return VIEW_TYPE;
	}
	public void setVIEW_TYPE(Integer vIEW_TYPE) {
		VIEW_TYPE = vIEW_TYPE;
	}
	public Integer getINVOICE_TYPE() {
		return INVOICE_TYPE;
	}
	public void setINVOICE_TYPE(Integer iNVOICE_TYPE) {
		INVOICE_TYPE = iNVOICE_TYPE;
	}

	public String getDIV_CODE() {
		return DIV_CODE;
	}
	public void setDIV_CODE(String dIV_CODE) {
		DIV_CODE = dIV_CODE;
	}
	public Double getTAXABLE_AMOUNT() {
		return TAXABLE_AMOUNT;
	}
	public void setTAXABLE_AMOUNT(Double tAXABLE_AMOUNT) {
		TAXABLE_AMOUNT = tAXABLE_AMOUNT;
	}
	public String getRETAILER_CODE() {
		return RETAILER_CODE;
	}
	public void setRETAILER_CODE(String rETAILER_CODE) {
		RETAILER_CODE = rETAILER_CODE;
	}
	public Timestamp getINVOICE_UPDATE() {
		return INVOICE_UPDATE;
	}
	public void setINVOICE_UPDATE(Timestamp iNVOICE_UPDATE) {
		INVOICE_UPDATE = iNVOICE_UPDATE;
	}
	public String getDISTRIBUTOR_NAME() {
		return DISTRIBUTOR_NAME;
	}
	public void setDISTRIBUTOR_NAME(String dISTRIBUTOR_NAME) {
		DISTRIBUTOR_NAME = dISTRIBUTOR_NAME;
	}
	public int getICON() {
		return ICON;
	}
	public void setICON(int iCON) {
		ICON = iCON;
	}
	public Integer getHAS_MAKER_CHECKER() {
		return HAS_MAKER_CHECKER;
	}
	public void setHAS_MAKER_CHECKER(Integer hAS_MAKER_CHECKER) {
		HAS_MAKER_CHECKER = hAS_MAKER_CHECKER;
	}
	public String getPAYEX_RETAILER_CODE() {
		return PAYEX_RETAILER_CODE;
	}
	public void setPAYEX_RETAILER_CODE(String pAYEX_RETAILER_CODE) {
		PAYEX_RETAILER_CODE = pAYEX_RETAILER_CODE;
	}
	public String getPAYEX_CPG_CODE() {
		return PAYEX_CPG_CODE;
	}
	public void setPAYEX_CPG_CODE(String pAYEX_CPG_CODE) {
		PAYEX_CPG_CODE = pAYEX_CPG_CODE;
	}
	public String getPAYEX_DISTRIBUTOR_CODE() {
		return PAYEX_DISTRIBUTOR_CODE;
	}
	public void setPAYEX_DISTRIBUTOR_CODE(String pAYEX_DISTRIBUTOR_CODE) {
		PAYEX_DISTRIBUTOR_CODE = pAYEX_DISTRIBUTOR_CODE;
	}
	public Integer getROUTE_CODE() {
		return ROUTE_CODE;
	}
	public void setROUTE_CODE(Integer rOUTE_CODE) {
		ROUTE_CODE = rOUTE_CODE;
	}
	public Integer getAGENT_CODE() {
		return AGENT_CODE;
	}
	public void setAGENT_CODE(int i) {
		AGENT_CODE = i;
	}
	public String getCPG_TYPE() {
		return CPG_TYPE;
	}
	public void setCPG_TYPE(String cPG_TYPE) {
		CPG_TYPE = cPG_TYPE;
	}
	public Integer getPAYEX_TRANSACTION_ID() {
		return PAYEX_TRANSACTION_ID;
	}
	public void setPAYEX_TRANSACTION_ID(Integer pAYEX_TRANSACTION_ID) {
		PAYEX_TRANSACTION_ID = pAYEX_TRANSACTION_ID;
	}
	public String getCPG_INVOICE_NUMBER() {
		return CPG_INVOICE_NUMBER;
	}
	public void setCPG_INVOICE_NUMBER(String cPG_INVOICE_NUMBER) {
		CPG_INVOICE_NUMBER = cPG_INVOICE_NUMBER;
	}
	public String getPAYEX_INVOICE_NUMBER() {
		return PAYEX_INVOICE_NUMBER;
	}
	public void setPAYEX_INVOICE_NUMBER(String pAYEX_INVOICE_NUMBER) {
		PAYEX_INVOICE_NUMBER = pAYEX_INVOICE_NUMBER;
	}
	public Date getCPG_INCVOICE_DATE() {
		return CPG_INCVOICE_DATE;
	}
	public void setCPG_INCVOICE_DATE(Date cPG_INCVOICE_DATE) {
		CPG_INCVOICE_DATE = cPG_INCVOICE_DATE;
	}
	public Date getDATE_TO_DUE() {
		return DATE_TO_DUE;
	}
	public void setDATE_TO_DUE(Date dATE_TO_DUE) {
		DATE_TO_DUE = dATE_TO_DUE;
	}
	public Double getCPG_INVOICE_AMOUNT() {
		return CPG_INVOICE_AMOUNT;
	}
	public void setCPG_INVOICE_AMOUNT(Double cPG_INVOICE_AMOUNT) {
		CPG_INVOICE_AMOUNT = cPG_INVOICE_AMOUNT;
	}
	public Double getTOTAL_AMOUNT_DUE() {
		return TOTAL_AMOUNT_DUE;
	}
	public void setTOTAL_AMOUNT_DUE(Double tOTAL_AMOUNT_DUE) {
		TOTAL_AMOUNT_DUE = tOTAL_AMOUNT_DUE;
	}
	public int getCPG_INVOICE_CREDIT() {
		return CPG_INVOICE_CREDIT;
	}
	public void setCPG_INVOICE_CREDIT(int cPG_INVOICE_CREDIT) {
		CPG_INVOICE_CREDIT = cPG_INVOICE_CREDIT;
	}
	public Timestamp getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(Timestamp cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
	public Timestamp getUPDATE_DATE() {
		return UPDATE_DATE;
	}
	public void setUPDATE_DATE(Timestamp uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}
	public Timestamp getDATE_TO_PRESENT() {
		return DATE_TO_PRESENT;
	}
	public void setDATE_TO_PRESENT(Timestamp dATE_TO_PRESENT) {
		DATE_TO_PRESENT = dATE_TO_PRESENT;
	}
	public Timestamp getRETAILER_ACKD_DATE_TIME() {
		return RETAILER_ACKD_DATE_TIME;
	}
	public void setRETAILER_ACKD_DATE_TIME(Timestamp rETAILER_ACKD_DATE_TIME) {
		RETAILER_ACKD_DATE_TIME = rETAILER_ACKD_DATE_TIME;
	}
	public Timestamp getDATETIME_APPROVED() {
		return DATETIME_APPROVED;
	}
	public void setDATETIME_APPROVED(Timestamp dATETIME_APPROVED) {
		DATETIME_APPROVED = dATETIME_APPROVED;
	}
	public Long getDATE_TO_PAY() {
		return DATE_TO_PAY;
	}
	public void setDATE_TO_PAY(Long dATE_TO_PAY) {
		DATE_TO_PAY = dATE_TO_PAY;
	}
	public String getAPPROVED_BANK_UMRN() {
		return APPROVED_BANK_UMRN;
	}
	public void setAPPROVED_BANK_UMRN(String aPPROVED_BANK_UMRN) {
		APPROVED_BANK_UMRN = aPPROVED_BANK_UMRN;
	}
	public String getAPPROVED_BANK() {
		return APPROVED_BANK;
	}
	public void setAPPROVED_BANK(String aPPROVED_BANK) {
		APPROVED_BANK = aPPROVED_BANK;
	}
	public String getRETAILER_REMARKS() {
		return RETAILER_REMARKS;
	}
	public void setRETAILER_REMARKS(String rETAILER_REMARKS) {
		RETAILER_REMARKS = rETAILER_REMARKS;
	}
	public String getAPRVD_OR_PAD_BY_DA_OR_RET() {
		return APRVD_OR_PAD_BY_DA_OR_RET;
	}
	public void setAPRVD_OR_PAD_BY_DA_OR_RET(String aPRVD_OR_PAD_BY_DA_OR_RET) {
		APRVD_OR_PAD_BY_DA_OR_RET = aPRVD_OR_PAD_BY_DA_OR_RET;
	}
	public String getRETAILER_MODE_OF_PAYMENT() {
		return RETAILER_MODE_OF_PAYMENT;
	}
	public void setRETAILER_MODE_OF_PAYMENT(String rETAILER_MODE_OF_PAYMENT) {
		RETAILER_MODE_OF_PAYMENT = rETAILER_MODE_OF_PAYMENT;
	}
	public Integer getACTIVE_FLAG() {
		return ACTIVE_FLAG;
	}
	public void setACTIVE_FLAG(Integer aCTIVE_FLAG) {
		ACTIVE_FLAG = aCTIVE_FLAG;
	}
	public Integer getPROCESS_STATUS() {
		return PROCESS_STATUS;
	}
	public void setPROCESS_STATUS(Integer pROCESS_STATUS) {
		PROCESS_STATUS = pROCESS_STATUS;
	}
	public Timestamp getFAILED_ON_DATE() {
		return FAILED_ON_DATE;
	}
	public void setFAILED_ON_DATE(Timestamp fAILED_ON_DATE) {
		FAILED_ON_DATE = fAILED_ON_DATE;
	}
	public Integer getACH_STATUS() {
		return ACH_STATUS;
	}
	public void setACH_STATUS(Integer aCH_STATUS) {
		ACH_STATUS = aCH_STATUS;
	}
	public Timestamp getRECEIVE_FROM_BANK_DATETIME() {
		return RECEIVE_FROM_BANK_DATETIME;
	}
	public void setRECEIVE_FROM_BANK_DATETIME(Timestamp rECEIVE_FROM_BANK_DATETIME) {
		RECEIVE_FROM_BANK_DATETIME = rECEIVE_FROM_BANK_DATETIME;
	}
	public Double getRECEIVE_FROM_BANK_AMOUNT() {
		return RECEIVE_FROM_BANK_AMOUNT;
	}
	public void setRECEIVE_FROM_BANK_AMOUNT(Double rECEIVE_FROM_BANK_AMOUNT) {
		RECEIVE_FROM_BANK_AMOUNT = rECEIVE_FROM_BANK_AMOUNT;
	}
	public String getBANK_TRANSACTION_ID() {
		return BANK_TRANSACTION_ID;
	}
	public void setBANK_TRANSACTION_ID(String bANK_TRANSACTION_ID) {
		BANK_TRANSACTION_ID = bANK_TRANSACTION_ID;
	}
	public String getPAYER_BANK_TRANSACTION_ID() {
		return PAYER_BANK_TRANSACTION_ID;
	}
	public void setPAYER_BANK_TRANSACTION_ID(String pAYER_BANK_TRANSACTION_ID) {
		PAYER_BANK_TRANSACTION_ID = pAYER_BANK_TRANSACTION_ID;
	}
	public Integer getNODAL_STATUS() {
		return NODAL_STATUS;
	}
	public void setNODAL_STATUS(Integer nODAL_STATUS) {
		NODAL_STATUS = nODAL_STATUS;
	}
	public String getPAYEX_TO_CPG_PYMNT_RFRNCE() {
		return PAYEX_TO_CPG_PYMNT_RFRNCE;
	}
	public void setPAYEX_TO_CPG_PYMNT_RFRNCE(String pAYEX_TO_CPG_PYMNT_RFRNCE) {
		PAYEX_TO_CPG_PYMNT_RFRNCE = pAYEX_TO_CPG_PYMNT_RFRNCE;
	}
	public Timestamp getPAYEX_TO_CPG_PAYMENT_DATE() {
		return PAYEX_TO_CPG_PAYMENT_DATE;
	}
	public void setPAYEX_TO_CPG_PAYMENT_DATE(Timestamp pAYEX_TO_CPG_PAYMENT_DATE) {
		PAYEX_TO_CPG_PAYMENT_DATE = pAYEX_TO_CPG_PAYMENT_DATE;
	}
	public Integer getLOYALTY_POINTS() {
		return LOYALTY_POINTS;
	}
	public void setLOYALTY_POINTS(Integer lOYALTY_POINTS) {
		LOYALTY_POINTS = lOYALTY_POINTS;
	}
	public Timestamp getUPDATE_COUNT() {
		return UPDATE_COUNT;
	}
	public void setUPDATE_COUNT(Timestamp uPDATE_COUNT) {
		UPDATE_COUNT = uPDATE_COUNT;
	}
	public String getINVOICE_INFO() {
		return INVOICE_INFO;
	}
	public void setINVOICE_INFO(String iNVOICE_INFO) {
		INVOICE_INFO = iNVOICE_INFO;
	}
	public String getINVOICE_INFO_EXTRA() {
		return INVOICE_INFO_EXTRA;
	}
	public void setINVOICE_INFO_EXTRA(String iNVOICE_INFO_EXTRA) {
		INVOICE_INFO_EXTRA = iNVOICE_INFO_EXTRA;
	}
	public String getINVOICE_SETTINGS() {
		return INVOICE_SETTINGS;
	}
	public void setINVOICE_SETTINGS(String iNVOICE_SETTINGS) {
		INVOICE_SETTINGS = iNVOICE_SETTINGS;
	}
	public int getINVOICE_FILE() {
		return INVOICE_FILE;
	}
	public void setINVOICE_FILE(int iNVOICE_FILE) {
		INVOICE_FILE = iNVOICE_FILE;
	}
	public void setAGENT_CODE(Integer aGENT_CODE) {
		AGENT_CODE = aGENT_CODE;
	}
	public Long getPAYEX_PAYMENT_ID() {
		return PAYEX_PAYMENT_ID;
	}
	public void setPAYEX_PAYMENT_ID(Long pAYEX_PAYMENT_ID) {
		PAYEX_PAYMENT_ID = pAYEX_PAYMENT_ID;
	}
	public String getDOC_REF() {
		return DOC_REF;
	}
	public void setDOC_REF(String dOC_REF) {
		DOC_REF = dOC_REF;
	}
	/**
	 * @return the aUTO_SCHEDULE
	 */
	public int getAUTO_SCHEDULE() {
		return AUTO_SCHEDULE;
	}
	/**
	 * @param aUTO_SCHEDULE the aUTO_SCHEDULE to set
	 */
	public void setAUTO_SCHEDULE(int aUTO_SCHEDULE) {
		AUTO_SCHEDULE = aUTO_SCHEDULE;
	}
	public String getRETAILER_TAG() {
		return RETAILER_TAG;
	}
	public void setRETAILER_TAG(String rETAILER_TAG) {
		RETAILER_TAG = rETAILER_TAG;
	}
	public double getAMOUNT_BANK() {
		return AMOUNT_BANK;
	}
	public void setAMOUNT_BANK(double aMOUNT_BANK) {
		AMOUNT_BANK = aMOUNT_BANK;
	}
	public double getAMOUNT_CN() {
		return AMOUNT_CN;
	}
	public void setAMOUNT_CN(double aMOUNT_CN) {
		AMOUNT_CN = aMOUNT_CN;
	}
	public long getPAYMENT_MODE() {
		return PAYMENT_MODE;
	}
	public void setPAYMENT_MODE(long pAYMENT_MODE) {
		PAYMENT_MODE = pAYMENT_MODE;
	}
	public double getAMOUNT_CD() {
		return AMOUNT_CD;
	}
	public void setAMOUNT_CD(double aMOUNT_CD) {
		AMOUNT_CD = aMOUNT_CD;
	}
	public String getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	
	public Timestamp getSETTLED_DATE() {
		return SETTLED_DATE;
	}
	public void setSETTLED_DATE(Timestamp sETTLED_DATE) {
		SETTLED_DATE = sETTLED_DATE;
	}
	public Timestamp getSETTLED_REVERT_DATE() {
		return SETTLED_REVERT_DATE;
	}
	public void setSETTLED_REVERT_DATE(Timestamp sETTLED_REVERT_DATE) {
		SETTLED_REVERT_DATE = sETTLED_REVERT_DATE;
	}
	public double getTcs() {
		return tcs;
	}
	public void setTcs(double tcs) {
		this.tcs = tcs;
	}
	public String getDOC_REF_NUMBER() {
		return DOC_REF_NUMBER;
	}
	public void setDOC_REF_NUMBER(String dOC_REF_NUMBER) {
		DOC_REF_NUMBER = dOC_REF_NUMBER;
	}
	
	
	
	/*@Override
	public String toString() {
		return "PAYEX_CENTRAL_INVOICES [PAYEX_RETAILER_CODE=" + PAYEX_RETAILER_CODE + ", PAYEX_CPG_CODE="
				+ PAYEX_CPG_CODE + ", PAYEX_DISTRIBUTOR_CODE=" + PAYEX_DISTRIBUTOR_CODE + ", ROUTE_CODE=" + ROUTE_CODE
				+ ", AGENT_CODE=" + AGENT_CODE + ", CPG_TYPE=" + CPG_TYPE + ", PAYEX_TRANSACTION_ID="
				+ PAYEX_TRANSACTION_ID + ", CPG_INVOICE_NUMBER=" + CPG_INVOICE_NUMBER + ", PAYEX_INVOICE_NUMBER="
				+ PAYEX_INVOICE_NUMBER + ", CPG_INCVOICE_DATE=" + CPG_INCVOICE_DATE + ", DATE_TO_DUE=" + DATE_TO_DUE
				+ ", CPG_INVOICE_AMOUNT=" + CPG_INVOICE_AMOUNT + ", TOTAL_AMOUNT_DUE=" + TOTAL_AMOUNT_DUE
				+ ", CPG_INVOICE_CREDIT=" + CPG_INVOICE_CREDIT + ", CREATE_DATE=" + CREATE_DATE + ", UPDATE_DATE="
				+ UPDATE_DATE + ", DATE_TO_PRESENT=" + DATE_TO_PRESENT + ", RETAILER_ACKD_DATE_TIME="
				+ RETAILER_ACKD_DATE_TIME + ", DATETIME_APPROVED=" + DATETIME_APPROVED + ", DATE_TO_PAY=" + DATE_TO_PAY
				+ ", APPROVED_BANK_UMRN=" + APPROVED_BANK_UMRN + ", APPROVED_BANK=" + APPROVED_BANK
				+ ", RETAILER_REMARKS=" + RETAILER_REMARKS + ", APRVD_OR_PAD_BY_DA_OR_RET=" + APRVD_OR_PAD_BY_DA_OR_RET
				+ ", RETAILER_MODE_OF_PAYMENT=" + RETAILER_MODE_OF_PAYMENT + ", ACTIVE_FLAG=" + ACTIVE_FLAG
				+ ", PROCESS_STATUS=" + PROCESS_STATUS + ", FAILED_ON_DATE=" + FAILED_ON_DATE + ", ACH_STATUS="
				+ ACH_STATUS + ", RECEIVE_FROM_BANK_DATETIME=" + RECEIVE_FROM_BANK_DATETIME
				+ ", RECEIVE_FROM_BANK_AMOUNT=" + RECEIVE_FROM_BANK_AMOUNT + ", BANK_TRANSACTION_ID="
				+ BANK_TRANSACTION_ID + ", PAYER_BANK_TRANSACTION_ID=" + PAYER_BANK_TRANSACTION_ID + ", NODAL_STATUS="
				+ NODAL_STATUS + ", PAYEX_TO_CPG_PYMNT_RFRNCE=" + PAYEX_TO_CPG_PYMNT_RFRNCE
				+ ", PAYEX_TO_CPG_PAYMENT_DATE=" + PAYEX_TO_CPG_PAYMENT_DATE + ", LOYALTY_POINTS=" + LOYALTY_POINTS
				+ ", UPDATE_COUNT=" + UPDATE_COUNT + ", INVOICE_INFO=" + INVOICE_INFO + ", INVOICE_INFO_EXTRA="
				+ INVOICE_INFO_EXTRA + ", INVOICE_SETTINGS=" + INVOICE_SETTINGS + ", HAS_MAKER_CHECKER="
				+ HAS_MAKER_CHECKER + ", DISTRIBUTOR_NAME=" + DISTRIBUTOR_NAME + ", ICON=" + ICON + ", INVOICE_UPDATE="
				+ INVOICE_UPDATE + ", RETAILER_CODE=" + RETAILER_CODE + ", TAXABLE_AMOUNT=" + TAXABLE_AMOUNT
				+ ", DIV_CODE=" + DIV_CODE + ", INVOICE_FILE=" + INVOICE_FILE + ", INVOICE_TYPE=" + INVOICE_TYPE + "]";
	}*/	
	
	
	
}
