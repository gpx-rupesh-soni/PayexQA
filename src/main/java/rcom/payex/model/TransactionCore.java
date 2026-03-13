package rcom.payex.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "PAYEX_TRANSACTION_CORE")
public class TransactionCore {
	
	@Id
	@Column(name = "PAYEX_PAYMENT_ID")
	long payexPaymentId ;	
	
	@Column(name = "PAYEX_DISTRIBUTOR_CODE")
	String payexDistributorCode ;
	
	@Column(name = "PAYEX_CPG_CODE")
	String payexCpgCode ;	
	
	@Column(name = "CREATE_DATE")
	Timestamp transactionDate ;
	
	@Column(name = "AMOUNT_TXN")
	double amountPresented ;
	
	@Column(name = "AMOUNT_APPROVED")
	double amountApproved ;
	
	@Column(name = "AMOUNT_CD")
	double amountCd ;	
	
	@Column(name = "AMOUNT_CN")
	double amountCn ;	
	
	@Column(name = "AMOUNT_BANK")
	double amountBank ;	
	
	@Column(name = "APPROVED_DATE")
	Timestamp approvedOn ;
	
	@Column(name = "PAYMENT_STATUS")
	int paymentStatus ;
	
	@Column(name = "BANK_PROCESS_IN")
	int ACH_STATUS ;
	
	@Column(name="BANK_PROCESS_OUT")
	int BANK_PROCESS_OUT;
	
	@Column(name = "PAYMENT_REMARKS")
	String remarks;	
	
	@Column(name = "PAYMENT_DATE")
	Date dateToPay;
	
	@Column(name="PAYMENT_MODE")
	Integer paymentMode;
	
	@Column(name = "PAYMENT_MODE_DESC")
	String paymentModeDesc;
	
	
	@Column(name="BANK_PROCESS_OUT_REF")
	String bankProcessOutRef;
	
	@Column(name = "APPROVED_BANK")
	String APPROVED_BANK;
		
	@Column(name="BANK_PROCESS_IN_REASON_CODE")
	String BANK_PROCESS_IN_REASON_CODE;
	
	@Column(name="BANK_PROCESS_IN_REASON_DESC")
	String BANK_PROCESS_IN_REASON_DESC;
	
	
	@Column(name="CPG_RETAILER_CODE")
	String CPG_RETAILER_CODE;
	
	@Column(name="NACH_APPLY_MODE")
	int NACH_APPLY_MODE;
	
	@Column(name="PAYEX_RETAILER_CODE")
	String payexRetailerCode;	
			
	public String getPAYEX_RETAILER_CODE() {
		return payexRetailerCode;
	}

	public int getNACH_APPLY_MODE() {
		return NACH_APPLY_MODE;
	}

	public void setNACH_APPLY_MODE(int nACH_APPLY_MODE) {
		NACH_APPLY_MODE = nACH_APPLY_MODE;
	}

	public String getCPG_RETAILER_CODE() {
		return CPG_RETAILER_CODE;
	}

	public void setCPG_RETAILER_CODE(String cPG_RETAILER_CODE) {
		CPG_RETAILER_CODE = cPG_RETAILER_CODE;
	}

	public String getBankProcessOutRef() {
		return bankProcessOutRef;
	}

	public void setBankProcessOutRef(String bankProcessOutRef) {
		this.bankProcessOutRef = bankProcessOutRef;
	}

	public int getBANK_PROCESS_OUT() {
		return BANK_PROCESS_OUT;
	}

	public void setBANK_PROCESS_OUT(int bANK_PROCESS_OUT) {
		BANK_PROCESS_OUT = bANK_PROCESS_OUT;
	}
	
		
	public String getBANK_PROCESS_IN_REASON_CODE() {
		return BANK_PROCESS_IN_REASON_CODE;
	}

	public void setBANK_PROCESS_IN_REASON_CODE(String bANK_PROCESS_IN_REASON_CODE) {
		BANK_PROCESS_IN_REASON_CODE = bANK_PROCESS_IN_REASON_CODE;
	}

	public String getBANK_PROCESS_IN_REASON_DESC() {
		return BANK_PROCESS_IN_REASON_DESC;
	}

	public void setBANK_PROCESS_IN_REASON_DESC(String bANK_PROCESS_IN_REASON_DESC) {
		BANK_PROCESS_IN_REASON_DESC = bANK_PROCESS_IN_REASON_DESC;
	}

	public Integer getPaymentMode() {
		return paymentMode;
	}
	
	public void setPaymentMode(Integer paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	public double getAmountPresented() {
		return amountPresented;
	}

	public void setAmountPresented(double amountPresented) {
		this.amountPresented = amountPresented;
	}

	public double getAmountApproved() {
		return amountApproved;
	}

	public void setAmountApproved(double amountApproved) {
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

	public String getAPPROVED_BANK() {
		return APPROVED_BANK;
	}

	public void setAPPROVED_BANK(String aPPROVED_BANK) {
		APPROVED_BANK = aPPROVED_BANK;
	}

	public int getACH_STATUS() {
		return ACH_STATUS;
	}

	public void setACH_STATUS(int aCH_STATUS) {
		ACH_STATUS = aCH_STATUS;
	}

	public double getAmountBank() {
		return amountBank;
	}

	public void setAmountBank(double amountBank) {
		this.amountBank = amountBank;
	}	
	
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Date getDateToPay() {
		return dateToPay;
	}
	public void setDateToPay(Date dateToPay) {
		this.dateToPay = dateToPay;
	}

	public long getPayexPaymentId() {
		return payexPaymentId;
	}

	public void setPayexPaymentId(long payexPaymentId) {
		this.payexPaymentId = payexPaymentId;
	}

	public String getPayexDistributorCode() {
		return payexDistributorCode;
	}

	public void setPayexDistributorCode(String payexDistributorCode) {
		this.payexDistributorCode = payexDistributorCode;
	}

	public String getPayexCpgCode() {
		return payexCpgCode;
	}

	public void setPayexCpgCode(String payexCpgCode) {
		this.payexCpgCode = payexCpgCode;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPayexRetailerCode() {
		return payexRetailerCode;
	}

	public void setPayexRetailerCode(String payexRetailerCode) {
		this.payexRetailerCode = payexRetailerCode;
	}

	public double getAmountCd() {
		return amountCd;
	}

	public void setAmountCd(double amountCd) {
		this.amountCd = amountCd;
	}

	public double getAmountCn() {
		return amountCn;
	}

	public void setAmountCn(double amountCn) {
		this.amountCn = amountCn;
	}

	public String getPaymentModeDesc() {
		return paymentModeDesc;
	}

	public void setPaymentModeDesc(String paymentModeDesc) {
		this.paymentModeDesc = paymentModeDesc;
	}

	
}
