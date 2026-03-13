package rcom.payex.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "PAYEX_CENTRAL_INVOICES")
public class InvoiceTransaction {
	
	@Id
	@Column(name = "PAYEX_TRANSACTION_ID")
	long transactionId ;
	
	//String shopName = "";
	
	@Column(name = "PAYEX_DISTRIBUTOR_CODE")
	String PAYEX_DISTRIBUTOR_CODE ;
	
	@Column(name = "PAYEX_CPG_CODE")
	String PAYEX_CPG_CODE ;
	
	@Column(name = "CPG_INVOICE_NUMBER")
	String invoiceNo ;	
	
	@Column(name = "CPG_INCVOICE_DATE")
	Timestamp invoiceDate ;
	
	@Column(name = "DATE_TO_DUE")
	Timestamp dateToDue ;
	
	@Column(name = "DATE_TO_PRESENT")
	Timestamp presentedOn ;
	
	@Column(name = "TOTAL_AMOUNT_DUE")
	Float amountPresented ;
	
	@Column(name = "AMOUNT_APPROVED")
	Float amountApproved ;
	
	@Column(name = "DATETIME_APPROVED")
	Timestamp approvedOn ;
	
	@Column(name = "PROCESS_STATUS")
	int processStatus ;
	
	@Column(name = "RETAILER_REMARKS")
	String remarks;
	
	@Column(name = "PAYEX_TO_CPG_PYMNT_RFRNCE")
	String txn_ref_no;
	
	@Column(name = "PAYEX_TO_CPG_PAYMENT_DATE")
	Timestamp nodal_txn_date ;
	
	@Column(name = "CREATE_DATE")
	Timestamp createDate ;
	
	@Column(name="UPDATE_DATE")
	Timestamp updateDate;
	
	@Column(name = "AGENT_CODE")
	int agentCode;
	
	@Column(name = "DATE_TO_PAY")
	Long dateToPay;
	
	@Column(name = "ROUTE_CODE")
	int routeCode;
	
	@Column(name = "RECEIVE_FROM_BANK_REMARKS")
	String bankRemarks;	
	
	// Lazy loading to make SQL more readable
	@ManyToOne 
	@JoinColumn(name = "PAYEX_RETAILER_CODE")
	private InvoiceRetailer retailer;
	
	@Column(name="ACTIVE_FLAG")
	int ACTIVE_FLAG;

	@Column(name="DISTRIBUTOR_NAME")
	String DISTRIBUTOR_NAME;
	
	@Column(name="CPG_INVOICE_CREDIT")
	int CPG_INVOICE_CREDIT;
	
	@Column(name="BANK_TRANSACTION_ID")
	String BANK_TRANSACTION_ID;
		
	@Column(name = "RECEIVE_FROM_BANK_DATETIME")
	Timestamp receive_FROM_BANK_DATETIME;
	
	@Column(name="ACH_STATUS")
	int ACH_STATUS;
	
	@Column(name="NACH_FILE_SEQUENCE_NUMBER")
	Long NACH_FILE_SEQUENCE_NUMBER;
	
	@Column(name="PAYEX_INVOICE_NUMBER")
	String PAYEX_INVOICE_NUMBER;
	
	@Column(name="PAYMENT_OPTION")
	int PAYMENT_OPTION;
			
	@Column(name="RETAILER_CODE")
	String RETAILER_CODE;
	
	@Column(name="CPG_INVOICE_AMOUNT")
	Double CPG_INVOICE_AMOUNT;
	
	@Column(name="AMOUNT_CD")
	Double AMOUNT_CD;
	
	@Column(name="APPROVED_CD_DETAILS")
	String APPROVED_CD_DETAILS;
	
	@Column(name="AMOUNT_CN")
	Double AMOUNT_CN;
	
	@Column(name="RETAILER_ACKD_DATE_TIME")
	Timestamp RETAILER_ACKD_DATE_TIME;
	
	
	@Column(name="AMOUNT_BANK")
	Double AMOUNT_BANK;
	
	@Column(name="CLIENT_VERSION")
	Integer CLIENT_VERSION;
	
	@Column(name="PAYEX_PAYMENT_ID")
	Long PAYEX_PAYMENT_ID;
	
	@Column(name="APPROVED_BANK")
	String APPROVED_BANK;
	
	@Column(name="PRESENTMENT_DATE")
	Date PRESENTMENT_DATE;
	
	@Column(name="PAYMENT_MODE")
	Integer PAYMENT_MODE;
	
	@Column(name="MAKER_APPROVAL_TIMESTAMP")
	Long MAKER_APPROVAL_TIMESTAMP;
	
	@Column(name="AMOUNT_SCD")
	Double AMOUNT_SCD;

	@Column(name="APPROVED_BANK_UMRN")
	String APPROVED_BANK_UMRN;
	
	@Column(name="APRVD_OR_PAD_BY_DA_OR_RET")
	String APRVD_OR_PAD_BY_DA_OR_RET;

	@Column(name="RETAILER_MODE_OF_PAYMENT")
	String RETAILER_MODE_OF_PAYMENT;
	
	
	@Column(name="RECEIVE_FROM_BANK_AMOUNT")
	Double RECEIVE_FROM_BANK_AMOUNT;

	@Column(name="CHECKED_BY_TIMESTAMP")
	Timestamp CHECKED_BY_TIMESTAMP;
	
	@Column(name="CHECKED_BY")
	String CHECKED_BY;
	
	@Column(name="PAYABLE_AMOUNT")
	Double PAYABLE_AMOUNT;
	
	@Transient
	private Long totalFailInvoice;
	
	@Transient
	private double totalAmount;

	public Timestamp getCHECKED_BY_TIMESTAMP() {
		return CHECKED_BY_TIMESTAMP;
	}

	public void setCHECKED_BY_TIMESTAMP(Timestamp cHECKED_BY_TIMESTAMP) {
		CHECKED_BY_TIMESTAMP = cHECKED_BY_TIMESTAMP;
	}

	public String getCHECKED_BY() {
		return CHECKED_BY;
	}

	public void setCHECKED_BY(String cHECKED_BY) {
		CHECKED_BY = cHECKED_BY;
	}

	public Double getRECEIVE_FROM_BANK_AMOUNT() {
		return RECEIVE_FROM_BANK_AMOUNT;
	}

	public void setRECEIVE_FROM_BANK_AMOUNT(Double rECEIVE_FROM_BANK_AMOUNT) {
		RECEIVE_FROM_BANK_AMOUNT = rECEIVE_FROM_BANK_AMOUNT;
	}

	public String getRETAILER_MODE_OF_PAYMENT() {
		return RETAILER_MODE_OF_PAYMENT;
	}

	public void setRETAILER_MODE_OF_PAYMENT(String rETAILER_MODE_OF_PAYMENT) {
		RETAILER_MODE_OF_PAYMENT = rETAILER_MODE_OF_PAYMENT;
	}

	public String getAPRVD_OR_PAD_BY_DA_OR_RET() {
		return APRVD_OR_PAD_BY_DA_OR_RET;
	}

	public void setAPRVD_OR_PAD_BY_DA_OR_RET(String aPRVD_OR_PAD_BY_DA_OR_RET) {
		APRVD_OR_PAD_BY_DA_OR_RET = aPRVD_OR_PAD_BY_DA_OR_RET;
	}

	public Double getAMOUNT_CD() {
		return AMOUNT_CD;
	}

	public void setAMOUNT_CD(Double aMOUNT_CD) {
		AMOUNT_CD = aMOUNT_CD;
	}

	public String getAPPROVED_CD_DETAILS() {
		return APPROVED_CD_DETAILS;
	}

	public void setAPPROVED_CD_DETAILS(String aPPROVED_CD_DETAILS) {
		APPROVED_CD_DETAILS = aPPROVED_CD_DETAILS;
	}

	public Double getAMOUNT_CN() {
		return AMOUNT_CN;
	}

	public void setAMOUNT_CN(Double aMOUNT_CN) {
		AMOUNT_CN = aMOUNT_CN;
	}

	public Timestamp getRETAILER_ACKD_DATE_TIME() {
		return RETAILER_ACKD_DATE_TIME;
	}

	public void setRETAILER_ACKD_DATE_TIME(Timestamp rETAILER_ACKD_DATE_TIME) {
		RETAILER_ACKD_DATE_TIME = rETAILER_ACKD_DATE_TIME;
	}

	public Double getAMOUNT_BANK() {
		return AMOUNT_BANK;
	}

	public void setAMOUNT_BANK(Double aMOUNT_BANK) {
		AMOUNT_BANK = aMOUNT_BANK;
	}

	public Integer getCLIENT_VERSION() {
		return CLIENT_VERSION;
	}

	public void setCLIENT_VERSION(Integer cLIENT_VERSION) {
		CLIENT_VERSION = cLIENT_VERSION;
	}

	public Long getPAYEX_PAYMENT_ID() {
		return PAYEX_PAYMENT_ID;
	}

	public void setPAYEX_PAYMENT_ID(Long pAYEX_PAYMENT_ID) {
		PAYEX_PAYMENT_ID = pAYEX_PAYMENT_ID;
	}

	public String getAPPROVED_BANK() {
		return APPROVED_BANK;
	}

	public void setAPPROVED_BANK(String aPPROVED_BANK) {
		APPROVED_BANK = aPPROVED_BANK;
	}

	public Date getPRESENTMENT_DATE() {
		return PRESENTMENT_DATE;
	}

	public void setPRESENTMENT_DATE(Date pRESENTMENT_DATE) {
		PRESENTMENT_DATE = pRESENTMENT_DATE;
	}

	public Integer getPAYMENT_MODE() {
		return PAYMENT_MODE;
	}

	public void setPAYMENT_MODE(Integer pAYMENT_MODE) {
		PAYMENT_MODE = pAYMENT_MODE;
	}

	public Long getMAKER_APPROVAL_TIMESTAMP() {
		return MAKER_APPROVAL_TIMESTAMP;
	}

	public void setMAKER_APPROVAL_TIMESTAMP(Long mAKER_APPROVAL_TIMESTAMP) {
		MAKER_APPROVAL_TIMESTAMP = mAKER_APPROVAL_TIMESTAMP;
	}

	public Double getAMOUNT_SCD() {
		return AMOUNT_SCD;
	}

	public void setAMOUNT_SCD(Double aMOUNT_SCD) {
		AMOUNT_SCD = aMOUNT_SCD;
	}

	public String getAPPROVED_BANK_UMRN() {
		return APPROVED_BANK_UMRN;
	}

	public void setAPPROVED_BANK_UMRN(String aPPROVED_BANK_UMRN) {
		APPROVED_BANK_UMRN = aPPROVED_BANK_UMRN;
	}

	public Double getCPG_INVOICE_AMOUNT() {
		return CPG_INVOICE_AMOUNT;
	}

	public void setCPG_INVOICE_AMOUNT(Double cPG_INVOICE_AMOUNT) {
		CPG_INVOICE_AMOUNT = cPG_INVOICE_AMOUNT;
	}

	public String getRETAILER_CODE() {
		return RETAILER_CODE;
	}

	public void setRETAILER_CODE(String rETAILER_CODE) {
		RETAILER_CODE = rETAILER_CODE;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getPAYEX_INVOICE_NUMBER() {
		return PAYEX_INVOICE_NUMBER;
	}

	public void setPAYEX_INVOICE_NUMBER(String pAYEX_INVOICE_NUMBER) {
		PAYEX_INVOICE_NUMBER = pAYEX_INVOICE_NUMBER;
	}

	public Long getNACH_FILE_SEQUENCE_NUMBER() {
		return NACH_FILE_SEQUENCE_NUMBER;
	}

	public void setNACH_FILE_SEQUENCE_NUMBER(Long nACH_FILE_SEQUENCE_NUMBER) {
		NACH_FILE_SEQUENCE_NUMBER = nACH_FILE_SEQUENCE_NUMBER;
	}

	public int getACH_STATUS() {
		return ACH_STATUS;
	}

	public void setACH_STATUS(int aCH_STATUS) {
		ACH_STATUS = aCH_STATUS;
	}

	/*public String getRECEIVE_FROM_BANK_REMARKS() {
		return RECEIVE_FROM_BANK_REMARKS;
	}

	public void setRECEIVE_FROM_BANK_REMARKS(String rECEIVE_FROM_BANK_REMARKS) {
		RECEIVE_FROM_BANK_REMARKS = rECEIVE_FROM_BANK_REMARKS;
	}*/
	
	public Timestamp getReceive_FROM_BANK_DATETIME() {
		return receive_FROM_BANK_DATETIME;
	}

	public void setReceive_FROM_BANK_DATETIME(Timestamp receive_FROM_BANK_DATETIME) {
		this.receive_FROM_BANK_DATETIME = receive_FROM_BANK_DATETIME;
	}


	public String getBANK_TRANSACTION_ID() {
		return BANK_TRANSACTION_ID;
	}

	public void setBANK_TRANSACTION_ID(String bANK_TRANSACTION_ID) {
		BANK_TRANSACTION_ID = bANK_TRANSACTION_ID;
	}

	public int getCPG_INVOICE_CREDIT() {
		return CPG_INVOICE_CREDIT;
	}

	public void setCPG_INVOICE_CREDIT(int cPG_INVOICE_CREDIT) {
		CPG_INVOICE_CREDIT = cPG_INVOICE_CREDIT;
	}

	public String getDISTRIBUTOR_NAME() {
		return DISTRIBUTOR_NAME;
	}

	public void setDISTRIBUTOR_NAME(String dISTRIBUTOR_NAME) {
		DISTRIBUTOR_NAME = dISTRIBUTOR_NAME;
	}

	public int getACTIVE_FLAG() {
		return ACTIVE_FLAG;
	}

	public void setACTIVE_FLAG(int aCTIVE_FLAG) {
		ACTIVE_FLAG = aCTIVE_FLAG;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getPAYEX_DISTRIBUTOR_CODE() {
		return PAYEX_DISTRIBUTOR_CODE;
	}

	public String getPAYEX_CPG_CODE() {
		return PAYEX_CPG_CODE;
	}

	public void setPAYEX_CPG_CODE(String pAYEX_CPG_CODE) {
		PAYEX_CPG_CODE = pAYEX_CPG_CODE;
	}

	public void setPAYEX_DISTRIBUTOR_CODE(String pAYEX_DISTRIBUTOR_CODE) {
		PAYEX_DISTRIBUTOR_CODE = pAYEX_DISTRIBUTOR_CODE;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Timestamp getPresentedOn() {
		return presentedOn;
	}

	public void setPresentedOn(Timestamp presentedOn) {
		this.presentedOn = presentedOn;
	}

	public Float getAmountPresented() {
		return amountPresented;
	}

	public void setAmountPresented(Float amountPresented) {
		this.amountPresented = amountPresented;
	}

	public Float getAmountApproved() {
		return amountApproved;
	}

	public void setAmountApproved(Float amountApproved) {
		this.amountApproved = amountApproved;
	}

	public Timestamp getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Timestamp approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public InvoiceRetailer getRetailer() {
		return retailer;
	}

	public void setRetailer(InvoiceRetailer retailer) {
		this.retailer = retailer;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTxn_ref_no() {
		return txn_ref_no;
	}

	public void setTxn_ref_no(String txn_ref_no) {
		this.txn_ref_no = txn_ref_no;
	}

	public int getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}

	public Timestamp getNodal_txn_date() {
		return nodal_txn_date;
	}

	public void setNodal_txn_date(Timestamp nodal_txn_date) {
		this.nodal_txn_date = nodal_txn_date;
	}

	public int getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(int agentCode) {
		this.agentCode = agentCode;
	}

	public int getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(int routeCode) {
		this.routeCode = routeCode;
	}

	public Long getDateToPay() {
		return dateToPay;
	}

	public void setDateToPay(Long dateToPay) {
		this.dateToPay = dateToPay;
	}

	public Timestamp getDateToDue() {
		return dateToDue;
	}

	public void setDateToDue(Timestamp dateToDue) {
		this.dateToDue = dateToDue;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public int getPAYMENT_OPTION() {
		return PAYMENT_OPTION;
	}

	public void setPAYMENT_OPTION(int pAYMENT_OPTION) {
		PAYMENT_OPTION = pAYMENT_OPTION;
	}

	public String getBankRemarks() {
		return bankRemarks;
	}

	public void setBankRemarks(String bankRemarks) {
		this.bankRemarks = bankRemarks;
	}

	public Double getPAYABLE_AMOUNT() {
		return PAYABLE_AMOUNT;
	}

	public void setPAYABLE_AMOUNT(Double pAYABLE_AMOUNT) {
		PAYABLE_AMOUNT = pAYABLE_AMOUNT;
	}

	public Long getTotalFailInvoice() {
		return totalFailInvoice;
	}

	public void setTotalFailInvoice(Long totalFailInvoice) {
		this.totalFailInvoice = totalFailInvoice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
}
