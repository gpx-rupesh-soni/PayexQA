package rcom.payex.service;

import java.util.HashMap;
import java.util.HashSet;
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

import rcom.payex.dao.RetailerDao;

@Service
public class QASupportService {
	
	Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	UrlService urlService;
	
	@Autowired
	RetailerDao retailerDao;

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
