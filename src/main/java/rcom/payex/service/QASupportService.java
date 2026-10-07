package rcom.payex.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import rcom.payex.dao.CommonsDao;
import rcom.payex.dao.InvoiceTransactionDao;
import rcom.payex.dao.RetailerDao;
import rcom.payex.model.CPG;
import rcom.payex.model.CPGDistributorMapping;
import rcom.payex.model.CentralInvoices;
import rcom.payex.model.Distributor;
import rcom.payex.model.DistributorSetting;
import rcom.payex.utils.DummyInvoiceGenerator;

@Service
public class QASupportService {

	Logger logger = LogManager.getLogger(getClass());

	@Autowired
	UrlService urlService;

	@Autowired
	RetailerDao retailerDao;
	
	@Autowired
	CommonsDao commonsDao;

	@Autowired
	InvoiceTransactionDao invoiceTransactionDao;

	private static final List<String> DUMMY_AREA_CODES = Arrays.asList("30", "29", "28", "12", "9");

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> generateDummyInvoices(String payexCpgCode,String retailerCode, String divCode, String docCode, double minAmt, double maxAmt, int docCount) {
		
		System.out.println("payexCpgCode : "+payexCpgCode);
		
		Integer viewType = 0;
		
		CPGDistributorMapping cpgDistributorMapping = commonsDao.getCPGDistributorMapping(payexCpgCode);
		
		CPG cpg = commonsDao.getCpg(payexCpgCode);
		
		String cpgTag = cpg.getCPG_TAG();
		System.out.println("cpgTag : "+cpgTag);
		
		String payexRetailerCode = retailerDao.getPayexRetailerCodeByDistRetMap(retailerCode, cpgTag).get(0);
		System.out.println("payexRetailerCode : "+payexRetailerCode);
		
		String payexDistributorCode = cpgDistributorMapping.getPAYEX_DISTRIBUTOR_CODE();
		System.out.println("payexDistributorCode : "+payexDistributorCode);
		
		Distributor distributor = commonsDao.getDistributor(payexDistributorCode);
		String distributorName = distributor.getDISTRIBUTOR_NAME();
		System.out.println("distributorName : "+distributorName);
		
		DistributorSetting distributorSetting = commonsDao.getDistributorSetting(payexDistributorCode);
		String invoiceSettings = distributorSetting.getINVOICE_SETTINGS();
		System.out.println("invoiceSettings : "+invoiceSettings);
		
		divCode = "09";
		docCode = "RV";		

		List<CentralInvoices> dummyInvoices = DummyInvoiceGenerator.generateDummyInvoices(payexCpgCode,
				payexDistributorCode, distributorName, payexRetailerCode, retailerCode, divCode, docCode, viewType,
				invoiceSettings, minAmt, maxAmt, DUMMY_AREA_CODES, docCount);

		int savedCount = invoiceTransactionDao.savePayexCentralInvoices(dummyInvoices);

		Map<String, Object> res = new HashMap<>();
		res.put("status", savedCount == docCount ? "SUCCESS" : "PARTIAL");
		res.put("generatedCount", savedCount);
		return res;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public org.json.JSONObject purgeData(String docType, String payexCpgCode,String retailerCode, String payexRetailerCode) {

		// payexCpgCode and (retailerCode or payexRetailerCode) are must, so it never deletes all data
		if (isBlank(payexCpgCode)) {
			throw new IllegalArgumentException("payexCpgCode is required");
		}
		if (isBlank(retailerCode) && isBlank(payexRetailerCode)) {
			throw new IllegalArgumentException("retailerCode or payexRetailerCode is required");
		}
		return invoiceTransactionDao.deleteRetailerData(docType, payexCpgCode, retailerCode, payexRetailerCode);
	}

	private static boolean isBlank(String codes) {
		// comma separated input like " , ," is treated as blank
		return codes == null || codes.replace(",", "").trim().isEmpty();
	}


	@Transactional(readOnly = true)
	public String exportLedgerData(String action, String data) {
		
		JSONObject responseJsonLedger = urlService.getLedgerData(data);
		//Set<String> buyerKeys = getUniqueKeys(responseJson);
		logger.info("responseJsonLedger :"+responseJsonLedger);		
		Map<String, Set<String>> cpgRetailerMap = getCpgRetailerMap(responseJsonLedger);
		logger.info("cpgRetailerMap :"+cpgRetailerMap);
		
		Map<String, Set<String>> nonExistretailers = retailerDao.getMissingRetailers(cpgRetailerMap);
		logger.info("nonExistretailers :"+nonExistretailers);
		
		JSONArray responseJsonBuyer = urlService.getBuyerData(new Gson().toJson(nonExistretailers));
		logger.info("responseJsonBuyer :"+responseJsonBuyer);	
		
		JSONArray responseJsonAddBuyer = urlService.addBuyerData(responseJsonBuyer.toJSONString());		
		logger.info("responseJsonAddBuyer :"+responseJsonAddBuyer);		
		
		JSONObject responseJsonAddLedger = urlService.addLedgerData(responseJsonLedger.toJSONString());		
		logger.info("responseJsonAddLedger :"+responseJsonAddLedger);
		
		return "";
	}
	
	public static Map<String, Set<String>> getCpgRetailerMap(JSONObject response) {

	    Map<String, Set<String>> map = new HashMap<>();

	    addToMap(response, "INV", map);
	    addToMap(response, "CN", map);

	    return map;
	}

	private static void addToMap(JSONObject response, String arrayName,
	                             Map<String, Set<String>> map) {

	    JSONArray array = (JSONArray) response.get(arrayName);

	    if (array == null) {
	        return;
	    }

	    for (Object obj : array) {
	        JSONObject json = (JSONObject) obj;

	        String cpgCode = (String) json.get("PAYEX_CPG_CODE");
	        String retailerCode = (String) json.get("RETAILER_CODE");

	        map.computeIfAbsent(cpgCode, k -> new HashSet<>())
	           .add(retailerCode);
	    }
	}
	
	
}
