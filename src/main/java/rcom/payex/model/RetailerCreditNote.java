package rcom.payex.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="RETAILER_CREDIT_NOTE")
public class RetailerCreditNote {

	@Id
	//@GenericGenerator(name="retailer_crt_note",strategy="rcom.payex.pojo.RetailerCreditNoteIdGenerator")
	//@GeneratedValue(generator="retailer_crt_note")
	@Column(name="PAYEX_CN_ID")
	long PAYEX_CN_ID;
	
	@Column(name="PAYEX_RETAILER_CODE")
	String PAYEX_RETAILER_CODE;
	
	@Column(name="PAYEX_DISTRIBUTOR_CODE")
	String PAYEX_DISTRIBUTOR_CODE;
	
	@Column(name="PAYEX_CPG_CODE")
	String PAYEX_CPG_CODE;
	
	@Column(name="DIVISION_CODE")
	String DIVISION_CODE;
	
	@Column(name="CPG_CN_ID")
	String CPG_CN_ID;
	
	@Column(name="CN_STATUS")
	int CN_STATUS;
	
	@Column(name="IS_PARTIALLY_USED")
	int IS_PARTIALLY_USED;
	
	@Column(name="CN_TYPE")
	String CN_TYPE;
	
	@Column(name="CN_FIRST_BALANCE")
	double CN_FIRST_BALANCE;
	
	@Column(name="CN_LAST_BALANCE")
	double CN_LAST_BALANCE;
	
	@Column(name="AMOUNT_USED")
	double AMOUNT_USED;
	
	@Column(name="CPG_STATRT_DATE")
	Date CPG_STATRT_DATE;

	@Column(name="CREATE_DATE")
	Timestamp CREATE_DATE; 
	
	@Column(name="UPDATE_DATE")
	Timestamp UPDATE_DATE; 
	
	@Column(name="CPG_END_DATE")
	Date CPG_END_DATE;
	//@Transient
	//String CIN;
		
	@Transient
	String GSTIN;
	
	@Column(name="VIEW_TYPE")
	Integer VIEW_TYPE;
	
	@Column(name="DOC_CODE")
	String DOC_CODE;

	@Column(name="INFO_EXTRA")
	String INFO_EXTRA;

	@Column(name="DOC_REF")
	String DOC_REF;
	
	@Column(name="RETAILER_CODE")
	String RETAILER_CODE;
	
	@Column(name ="FINANCIAL_YEAR")
	private Integer FINANCIAL_YEAR;
	
	@Column(name="CN_INVOICE_CHECK")
	private String CN_INVOICE_CHECK;
	
	@Column(name="SEQUENCE_FLAG")
	String SEQUENCE_FLAG="S00";
	
	@Column(name="SETTLED_DATE")
	private Timestamp SETTLED_DATE;
	
	@Column(name="SETTLED_REVERT_DATE")
	private Timestamp SETTLED_REVERT_DATE;
	
	@Column(name="CLEARING_DOC")
	String CLEARING_DOC;

	//@author RahulB 21-07-2021 Sanyo pay ref added
	@Column(name = "PAY_REF")
	String payRef;
	
	@Column(name="CN_CATEGORY")
	int cnCategory;
	
	@Override
	public String toString() {
		return "RetailerCreditNote [PAYEX_CN_ID=" + PAYEX_CN_ID + ", PAYEX_RETAILER_CODE=" + PAYEX_RETAILER_CODE
				+ ", PAYEX_DISTRIBUTOR_CODE=" + PAYEX_DISTRIBUTOR_CODE + ", PAYEX_CPG_CODE=" + PAYEX_CPG_CODE
				+ ", DIVISION_CODE=" + DIVISION_CODE + ", CPG_CN_ID=" + CPG_CN_ID + ", CN_STATUS=" + CN_STATUS
				+ ", CN_TYPE=" + CN_TYPE + ", CN_FIRST_BALANCE=" + CN_FIRST_BALANCE + ", CN_LAST_BALANCE="
				+ CN_LAST_BALANCE + ", CPG_STATRT_DATE=" + CPG_STATRT_DATE + ", CREATE_DATE=" + CREATE_DATE
				+ ", UPDATE_DATE=" + UPDATE_DATE + ", CPG_END_DATE=" + CPG_END_DATE + ", GSTIN=" + GSTIN
				+ ", VIEW_TYPE=" + VIEW_TYPE + ", DOC_CODE=" + DOC_CODE + ", INFO_EXTRA=" + INFO_EXTRA + ", DOC_REF="
				+ DOC_REF + ", RETAILER_CODE=" + RETAILER_CODE + ", FINANCIAL_YEAR=" + FINANCIAL_YEAR
				+ ", CN_INVOICE_CHECK=" + CN_INVOICE_CHECK + ", SEQUENCE_FLAG=" + SEQUENCE_FLAG + "]";
	}

	public String getSEQUENCE_FLAG() {
		return SEQUENCE_FLAG;
	}

	public void setSEQUENCE_FLAG(String sEQUENCE_FLAG) {
		SEQUENCE_FLAG = sEQUENCE_FLAG;
	}

	public String getCN_INVOICE_CHECK() {
		return CN_INVOICE_CHECK;
	}

	public void setCN_INVOICE_CHECK(String cN_INVOICE_CHECK) {
		CN_INVOICE_CHECK = cN_INVOICE_CHECK;
	}

	public Integer getFINANCIAL_YEAR() {
		return FINANCIAL_YEAR;
	}

	public void setFINANCIAL_YEAR(Integer fINANCIAL_YEAR) {
		FINANCIAL_YEAR = fINANCIAL_YEAR;
	}

	public String getRETAILER_CODE() {
		return RETAILER_CODE;
	}

	public void setRETAILER_CODE(String rETAILER_CODE) {
		RETAILER_CODE = rETAILER_CODE;
	}

	public String getINFO_EXTRA() {
		return INFO_EXTRA;
	}

	public void setINFO_EXTRA(String iNFO_EXTRA) {
		INFO_EXTRA = iNFO_EXTRA;
	}

	public String getDOC_CODE() {
		return DOC_CODE;
	}

	public void setDOC_CODE(String dOC_CODE) {
		DOC_CODE = dOC_CODE;
	}

	public Integer getVIEW_TYPE() {
		return VIEW_TYPE;
	}

	public void setVIEW_TYPE(Integer vIEW_TYPE) {
		VIEW_TYPE = vIEW_TYPE;
	}

	public Date getCPG_END_DATE() {
		return CPG_END_DATE;
	}

	public void setCPG_END_DATE(Date cPG_END_DATE) {
		CPG_END_DATE = cPG_END_DATE;
	}

	public Timestamp getUPDATE_DATE() {
		return UPDATE_DATE;
	}

	public void setUPDATE_DATE(Timestamp uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	public Timestamp getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(Timestamp cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String gSTIN) {
		GSTIN = gSTIN;
	}

	public long getPAYEX_CN_ID() {
		return PAYEX_CN_ID;
	}

	public String getPAYEX_RETAILER_CODE() {
		return PAYEX_RETAILER_CODE;
	}

	public String getPAYEX_DISTRIBUTOR_CODE() {
		return PAYEX_DISTRIBUTOR_CODE;
	}

	public String getCPG_CN_ID() {
		return CPG_CN_ID;
	}

	public int getCN_STATUS() {
		return CN_STATUS;
	}

	public String getCN_TYPE() {
		return CN_TYPE;
	}

	public double getCN_FIRST_BALANCE() {
		return CN_FIRST_BALANCE;
	}

	public double getCN_LAST_BALANCE() {
		return CN_LAST_BALANCE;
	}

	public Date getCPG_STATRT_DATE() {
		return CPG_STATRT_DATE;
	}

	public void setPAYEX_CN_ID(long pAYEX_CN_ID) {
		PAYEX_CN_ID = pAYEX_CN_ID;
	}

	public void setPAYEX_RETAILER_CODE(String pAYEX_RETAILER_CODE) {
		PAYEX_RETAILER_CODE = pAYEX_RETAILER_CODE;
	}

	public void setPAYEX_DISTRIBUTOR_CODE(String pAYEX_DISTRIBUTOR_CODE) {
		PAYEX_DISTRIBUTOR_CODE = pAYEX_DISTRIBUTOR_CODE;
	}

	public void setCPG_CN_ID(String cPG_CN_ID) {
		CPG_CN_ID = cPG_CN_ID;
	}

	public void setCN_STATUS(int cN_STATUS) {
		CN_STATUS = cN_STATUS;
	}

	public void setCN_TYPE(String cN_TYPE) {
		CN_TYPE = cN_TYPE;
	}

	public void setCN_FIRST_BALANCE(double cN_FIRST_BALANCE) {
		CN_FIRST_BALANCE = cN_FIRST_BALANCE;
	}

	public void setCN_LAST_BALANCE(double cN_LAST_BALANCE) {
		CN_LAST_BALANCE = cN_LAST_BALANCE;
	}

	public void setCPG_STATRT_DATE(Date cPG_STATRT_DATE) {
		CPG_STATRT_DATE = cPG_STATRT_DATE;
	}

	public String getPAYEX_CPG_CODE() {
		return PAYEX_CPG_CODE;
	}

	public void setPAYEX_CPG_CODE(String pAYEX_CPG_CODE) {
		PAYEX_CPG_CODE = pAYEX_CPG_CODE;
	}

	public String getDIVISION_CODE() {
		return DIVISION_CODE;
	}

	public void setDIVISION_CODE(String dIVISION_CODE) {
		DIVISION_CODE = dIVISION_CODE;
	}

	public String getDOC_REF() {
		return DOC_REF;
	}

	public void setDOC_REF(String dOC_REF) {
		DOC_REF = dOC_REF;
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

	public String getCLEARING_DOC() {
		return CLEARING_DOC;
	}

	public void setCLEARING_DOC(String cLEARING_DOC) {
		CLEARING_DOC = cLEARING_DOC;
	}

	public String getPayRef() {
		return payRef;
	}

	public void setPayRef(String payRef) {
		this.payRef = payRef;
	}

	public int getCnCategory() {
		return cnCategory;
	}

	public void setCnCategory(int cnCategory) {
		this.cnCategory = cnCategory;
	}

	public int getIS_PARTIALLY_USED() {
		return IS_PARTIALLY_USED;
	}

	public void setIS_PARTIALLY_USED(int iS_PARTIALLY_USED) {
		IS_PARTIALLY_USED = iS_PARTIALLY_USED;
	}

	public double getAMOUNT_USED() {
		return AMOUNT_USED;
	}

	public void setAMOUNT_USED(double aMOUNT_USED) {
		AMOUNT_USED = aMOUNT_USED;
	}
	
}
