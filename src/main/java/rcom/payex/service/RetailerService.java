package rcom.payex.service;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rcom.payex.dao.CommonsDao;
import rcom.payex.dao.InvoiceTransactionDao;
import rcom.payex.dao.RetailerDao;
import rcom.payex.model.CPG;
import rcom.payex.model.Retailer;


@Service
public class RetailerService {

	@Autowired
	RetailerDao retailerDao;
	
	@Autowired
	CommonsDao commonsDao;
	
	@Autowired
	InvoiceTransactionDao transactionDao;
	
	
	@Transactional(readOnly = true)
	public Retailer getRetailer(String payexRetailerCode) {
		return retailerDao.getRetailer(payexRetailerCode);
	}
	
	@Transactional(readOnly = true)
	public Retailer getRetailerWithOtherDetails(String payexRetailerCode) {
		Retailer ret = retailerDao.getRetailer(payexRetailerCode);
		if(ret!=null) {
			System.out.println("ret : "+ret.getShop_NAME());
			List<CPG> cpgs = commonsDao.getCpgs(Arrays.asList("CPGVGUARD,CPGBRGSTN,CPGMTLS".split(",")));		
			List<String> cpgTags = retailerDao.getRetailerCpg(payexRetailerCode,cpgs);
			System.out.println("cpgTags : "+cpgTags);
			ret.setCpgTag(cpgTags);
		}
		return ret;
	}
	
	@Transactional(readOnly = true)
	public Retailer getRetailerByCpg(String payexCpgCode, String cpgRetailerCode) {	
		System.out.println("cpgRetailerCode : "+cpgRetailerCode);
		System.out.println("payexCpgCode : "+payexCpgCode);
		CPG cpg = commonsDao.getCpg(payexCpgCode);
		Retailer ret = null;
		if(cpg!=null) {
			List<String> retList = retailerDao.getPayexRetailerCodeByDistRetMap(cpgRetailerCode, cpg.getCPG_TAG());
			if(retList.size()>0) {
				ret = retailerDao.getRetailer(retList.get(0));
				ret.setCpgTag(Arrays.asList(new String[]{payexCpgCode}));
			}
		}
	
		return null;
	}
	
	@Transactional(readOnly = true)
	public Retailer getRetailer(String username, String password) {
		return retailerDao.getRetailer(username, password);
	}
	
	@Transactional
	public JSONObject updateRetailer(String type, Retailer retailer) {		
		
		switch (type) {
		
		case "up-mobile":
			retailerDao.updateRetailerMobileForQA(retailer);
			break;

		case "up-info":
			retailerDao.updateRetailerInfo(retailer);
			break;

		case "up-pin":
			retailer.setPin("b59c67bf196a4758191e42f76670ceba");
			retailerDao.updateRetailerPin(retailer);
			break;
			
		case "del-data":
			return transactionDao.deleteRetailerData(retailer.getPayex_RETAILER_CODE());			
			
		case "del-ret":			
			return retailerDao.deleteRetailer(retailer,retailer.getCpgTag());
			
		default:
			break;
		}
		
		return new JSONObject();
	}
		
}
