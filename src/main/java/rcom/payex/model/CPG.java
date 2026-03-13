package rcom.payex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CPG_MASTER")
public class CPG {
	
	@Id
	@Column(name = "PAYEX_CPG_CODE")
	private String PAYEX_CPG_CODE;
	
	@Column(name = "NAME")
	private String NAME;
	
	@Column(name = "CPG_LOGO")
	private String CPG_LOGO;
	
	@Column(name = "REG_DATE")
	private Date REG_DATE;
	
	@Column(name = "CPG_TAG")
	private String CPG_TAG;

	public String getPAYEX_CPG_CODE() {
		return PAYEX_CPG_CODE;
	}

	public void setPAYEX_CPG_CODE(String pAYEX_CPG_CODE) {
		PAYEX_CPG_CODE = pAYEX_CPG_CODE;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getCPG_LOGO() {
		return CPG_LOGO;
	}

	public void setCPG_LOGO(String cPG_LOGO) {
		CPG_LOGO = cPG_LOGO;
	}

	public Date getREG_DATE() {
		return REG_DATE;
	}

	public void setREG_DATE(Date rEG_DATE) {
		REG_DATE = rEG_DATE;
	}

	public String getCPG_TAG() {
		return CPG_TAG;
	}

	public void setCPG_TAG(String cPG_TAG) {
		CPG_TAG = cPG_TAG;
	}

	

}
