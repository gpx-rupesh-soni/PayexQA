package rcom.payex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CPG_DISTRIBUTOR_MAPPING")
public class CPGDistributorMapping {

	@Id
	@Column(name = "CPG_DISTRIBUTOR_ID")
	private Integer CPG_DISTRIBUTOR_ID;

	@Column(name = "PAYEX_CPG_CODE")
	private String PAYEX_CPG_CODE;
	
	@Column(name = "PAYEX_DISTRIBUTOR_CODE")
	private String PAYEX_DISTRIBUTOR_CODE;

	public Integer getCPG_DISTRIBUTOR_ID() {
		return CPG_DISTRIBUTOR_ID;
	}

	public void setCPG_DISTRIBUTOR_ID(Integer cPG_DISTRIBUTOR_ID) {
		CPG_DISTRIBUTOR_ID = cPG_DISTRIBUTOR_ID;
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
	
	

}
