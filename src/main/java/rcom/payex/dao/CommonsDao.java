package rcom.payex.dao;

import java.util.List;

import rcom.payex.model.CPG;
import rcom.payex.model.CPGDistributorMapping;
import rcom.payex.model.Distributor;

/** InvoiceTransactionDao */
public interface CommonsDao {	
	
	public CPG getCpg(String payexCpgCode);
	public List<CPG> getCpgs(List<String> payexCpgCodes);
	public Distributor getDistributor(String payexDistributorCode);
	public CPGDistributorMapping getCPGDistributorMapping(String payexCpgCode);
	
	
}
