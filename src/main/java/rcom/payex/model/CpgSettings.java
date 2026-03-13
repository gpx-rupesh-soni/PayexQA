package rcom.payex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CPG_SETTINGS")
public class CpgSettings {
	
	@Id
	@Column(name = "CPG_SETTING_ID")
	int CPG_SETTING_ID;
	
	@Column(name = "PAYEX_CPG_CODE")
	String PAYEX_CPG_CODE;
	
	@Column(name = "ALLOW_NACH_DR")
	int ALLOW_NACH_DR;
	
	@Column(name = "ALLOW_NODAL_PAYOUT")
	int ALLOW_NODAL_PAYOUT;

	public int getCPG_SETTING_ID() {
		return CPG_SETTING_ID;
	}

	public void setCPG_SETTING_ID(int cPG_SETTING_ID) {
		CPG_SETTING_ID = cPG_SETTING_ID;
	}

	public String getPAYEX_CPG_CODE() {
		return PAYEX_CPG_CODE;
	}

	public void setPAYEX_CPG_CODE(String pAYEX_CPG_CODE) {
		PAYEX_CPG_CODE = pAYEX_CPG_CODE;
	}

	public int getALLOW_NACH_DR() {
		return ALLOW_NACH_DR;
	}

	public void setALLOW_NACH_DR(int aLLOW_NACH_DR) {
		ALLOW_NACH_DR = aLLOW_NACH_DR;
	}

	public int getALLOW_NODAL_PAYOUT() {
		return ALLOW_NODAL_PAYOUT;
	}

	public void setALLOW_NODAL_PAYOUT(int aLLOW_NODAL_PAYOUT) {
		ALLOW_NODAL_PAYOUT = aLLOW_NODAL_PAYOUT;
	}
	
	
	
}
