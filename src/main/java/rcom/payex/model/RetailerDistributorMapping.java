package rcom.payex.model;

import java.math.BigDecimal;

public class RetailerDistributorMapping {

	private String PAYEX_RETAILER_CODE;
	private String PAYEX_DISTRIBUTOR_CODE;
	private String CPG_RETAILER_CODE;
	private int IS_ACTIVE;
	
	public String getPAYEX_RETAILER_CODE() {
		return PAYEX_RETAILER_CODE;
	}
	public void setPAYEX_RETAILER_CODE(String pAYEX_RETAILER_CODE) {
		PAYEX_RETAILER_CODE = pAYEX_RETAILER_CODE;
	}
	public String getPAYEX_DISTRIBUTOR_CODE() {
		return PAYEX_DISTRIBUTOR_CODE;
	}
	public void setPAYEX_DISTRIBUTOR_CODE(String pAYEX_DISTRIBUTOR_CODE) {
		PAYEX_DISTRIBUTOR_CODE = pAYEX_DISTRIBUTOR_CODE;
	}
	public String getCPG_RETAILER_CODE() {
		return CPG_RETAILER_CODE;
	}
	public void setCPG_RETAILER_CODE(String cPG_RETAILER_CODE) {
		CPG_RETAILER_CODE = cPG_RETAILER_CODE;
	}
	public int getIS_ACTIVE() {
		return IS_ACTIVE;
	}
	public void setIS_ACTIVE(int iS_ACTIVE) {
		IS_ACTIVE = iS_ACTIVE;
	}	
}
