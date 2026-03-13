package rcom.payex.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rcom.payex.dao.CommonsDao;
import rcom.payex.dao.RetailerDao;
import rcom.payex.model.CPG;
import rcom.payex.model.CPGDistributorMapping;
import rcom.payex.model.CentralInvoices;
import rcom.payex.model.Distributor;
import rcom.payex.model.Retailer;
import rcom.payex.utils.CentralInvoiceUtil;

@Component
public class CommonComponent {
	
	@Autowired
	RetailerDao retailerDao;
	
	@Autowired
	CommonsDao commonsDao;

	
	@Transactional(readOnly = true)
	public List<CentralInvoices> generateLedgerData(String payexCpgCode, String payexRetailerCode, String cpgInvoiceNumber) {
		try {
			
			Retailer retailer =  retailerDao.getRetailer(payexRetailerCode);	
			CPG cpg =  commonsDao.getCpg(payexCpgCode);
			CPGDistributorMapping cpgDistributorMapping = commonsDao.getCPGDistributorMapping(cpg.getPAYEX_CPG_CODE());
			Distributor distributor = commonsDao.getDistributor(cpg.getPAYEX_CPG_CODE());
			
			
			
		
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
