package rcom.payex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DISTRIBUTOR_MASTER")
public class Distributor {

	@Id
	@Column(name = "PAYEX_DISTRIBUTOR_CODE")
	private String PAYEX_DISTRIBUTOR_CODE;
	
	@Column(name = "DISTRIBUTOR_CODE")
	private String DISTRIBUTOR_CODE;
	
	@Column(name = "DISTRIBUTOR_NAME")
	private String DISTRIBUTOR_NAME;
	
	@Column(name = "DISTRIBUTOR_LOGO")
	private String DISTRIBUTOR_LOGO;
	
	@Column(name = "CREATED_AT")
	private Date CREATED_AT;
	
	/*@Column(name = "USER_NAME")
	private String USER_NAME;
	
	@Column(name = "PASSWORD")
	private String PASSWORD;*/
	
	@Column(name = "ADDRESS")
	private String ADDRESS;
	
	@Column(name = "COMPANY_NAME")
	private String COMPANY_NAME;
	
	@Column(name = "EMAIL")
	private String EMAIL;
	
	@Column(name = "MOBILE")
	private String MOBILE;
	
	@Column(name = "LANDLINE")
	private String LANDLINE;
	
	
	public Distributor(){}
	
	public Distributor(String dISTRIBUTOR_CODE, String dISTRIBUTOR_NAME) {
		PAYEX_DISTRIBUTOR_CODE = dISTRIBUTOR_CODE;
		DISTRIBUTOR_NAME = dISTRIBUTOR_NAME;
	}
	
	public String getDISTRIBUTOR_CODE() {
		return DISTRIBUTOR_CODE;
	}
	public void setDISTRIBUTOR_CODE(String dISTRIBUTOR_CODE) {
		DISTRIBUTOR_CODE = dISTRIBUTOR_CODE;
	}
	public String getDISTRIBUTOR_NAME() {
		return DISTRIBUTOR_NAME;
	}
	public void setDISTRIBUTOR_NAME(String dISTRIBUTOR_NAME) {
		DISTRIBUTOR_NAME = dISTRIBUTOR_NAME;
	}
	public String getDISTRIBUTOR_LOGO() {
		return DISTRIBUTOR_LOGO;
	}
	public void setDISTRIBUTOR_LOGO(String dISTRIBUTOR_LOGO) {
		DISTRIBUTOR_LOGO = dISTRIBUTOR_LOGO;
	}
	public String getPAYEX_DISTRIBUTOR_CODE() {
		return PAYEX_DISTRIBUTOR_CODE;
	}
	public void setPAYEX_DISTRIBUTOR_CODE(String pAYEX_DISTRIBUTOR_CODE) {
		PAYEX_DISTRIBUTOR_CODE = pAYEX_DISTRIBUTOR_CODE;
	}
	public Date getCREATED_AT() {
		return CREATED_AT;
	}
	public void setCREATED_AT(Date cREATED_AT) {
		CREATED_AT = cREATED_AT;
	}
	/*public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}*/
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getLANDLINE() {
		return LANDLINE;
	}
	public void setLANDLINE(String lANDLINE) {
		LANDLINE = lANDLINE;
	}
	
	
	
}
