package rcom.payex.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RETAILER_VACC_MASTER")
public class RetailerVacc implements Serializable {
	
	@Id
	@Column(name = "CPG_RETAILER_CODE")
	private String cpgRetailerCode;
	
	@Id
	@Column(name = "PAYEX_RETAILER_CODE")
	private String payexRetailerCode;
	
	@Id
	@Column(name = "PAYEX_CPG_CODE")
	private String payexCpgCode;
	
	@Id
	@Column(name = "PAYEX_DISTRIBUTOR_CODE")
	private String payexDistributorCode;
	
	@Id
	@Column(name = "VACC_NO")
	private String vaccNo;
	
	@Id
	@Column(name = "BANK_TAG")
	private String bankTag;
	
	@Column(name = "REG_FLAG")
	private int regFlag;
	
	public String getCpgRetailerCode() {
		return cpgRetailerCode;
	}

	public void setCpgRetailerCode(String cpgRetailerCode) {
		this.cpgRetailerCode = cpgRetailerCode;
	}

	public String getBankTag() {
		return bankTag;
	}

	public void setBankTag(String bankTag) {
		this.bankTag = bankTag;
	}

	public String getPayexCpgCode() {
		return payexCpgCode;
	}

	public void setPayexCpgCode(String payexCpgCode) {
		this.payexCpgCode = payexCpgCode;
	}

	public String getPayexDistributorCode() {
		return payexDistributorCode;
	}

	public void setPayexDistributorCode(String payexDistributorCode) {
		this.payexDistributorCode = payexDistributorCode;
	}

	public String getVaccNo() {
		return vaccNo;
	}

	public void setVaccNo(String vaccNo) {
		this.vaccNo = vaccNo;
	}

	public String getPayexRetailerCode() {
		return payexRetailerCode;
	}

	public void setPayexRetailerCode(String payexRetailerCode) {
		this.payexRetailerCode = payexRetailerCode;
	}

	public int getRegFlag() {
		return regFlag;
	}

	public void setRegFlag(int regFlag) {
		this.regFlag = regFlag;
	}
	
}
