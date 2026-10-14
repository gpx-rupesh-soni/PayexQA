package rcom.payex.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import rcom.payex.utils.ApplicationConstant;
import rcom.payex.utils.DummyInvoiceGenerator;
import rcom.payex.utils.Utils;

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

	public static final String PURGE_FOR_LEDGER = "ledger";
	public static final String PURGE_FOR_VIEW_FILE = "viewfile";

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
	public org.json.JSONObject purgeData(String purgeFor, String docType, String payexCpgCode,String retailerCode, String payexRetailerCode) throws JSONException {

		// payexCpgCode and (retailerCode or payexRetailerCode) are must, so it never deletes all data
		if (isBlank(payexCpgCode)) {
			throw new IllegalArgumentException("payexCpgCode is required");
		}
		if (isBlank(retailerCode) && isBlank(payexRetailerCode)) {
			throw new IllegalArgumentException("retailerCode or payexRetailerCode is required");
		}

		if (PURGE_FOR_LEDGER.equalsIgnoreCase(purgeFor)) {
			return invoiceTransactionDao.deleteRetailerData(docType, payexCpgCode, retailerCode, payexRetailerCode);
		}
		if (PURGE_FOR_VIEW_FILE.equalsIgnoreCase(purgeFor)) {
			return purgeViewFiles(docType, payexCpgCode.trim(), retailerCode, payexRetailerCode);
		}
		throw new IllegalArgumentException("purgeFor must be " + PURGE_FOR_LEDGER + " or " + PURGE_FOR_VIEW_FILE);
	}

	// file path : {invoice data dir}/{payexCpgCode}/{folder of VIEW_TYPE}/[{doc date year}/ only for CPGVGUARD]{file name}{extension of VIEW_TYPE}
	// file name : doc number for INV, {payex distributor code}-{doc number} for CN
	private org.json.JSONObject purgeViewFiles(String docType, String payexCpgCode, String retailerCode, String payexRetailerCode) throws JSONException {

		org.json.JSONObject res = new org.json.JSONObject();

		Path cpgDir = Paths.get(ApplicationConstant.getInvoiceDataDir(), payexCpgCode).toAbsolutePath().normalize();
		logger.info("cpgDir : "+cpgDir.toAbsolutePath().toString());

		List<Object[]> docs = invoiceTransactionDao.getRetailerViewFileDocs(docType, payexCpgCode, retailerCode, payexRetailerCode);

		org.json.JSONArray deleted = new org.json.JSONArray();
		org.json.JSONArray notFound = new org.json.JSONArray();
		org.json.JSONArray failed = new org.json.JSONArray();

		boolean yearFolder = "CPGVGUARD".equals(payexCpgCode);
		Calendar cal = Calendar.getInstance();
		for (Object[] doc : docs) {
			// doc : [doc kind (INV / CN), doc number, doc date, view type, payex distributor code]
			boolean isCn = "CN".equals(doc[0]);
			String docNumber = doc[1] == null ? "" : doc[1].toString().trim();
			if (docNumber.isEmpty() || (yearFolder && !(doc[2] instanceof Date))) {
				failed.put(docNumber + " : missing doc number or doc date");
				continue;
			}
			String distributorCode = doc[4] == null ? "" : doc[4].toString().trim();
			if (isCn && distributorCode.isEmpty()) {
				failed.put(docNumber + " : missing payex distributor code");
				continue;
			}
			String fileName = isCn ? distributorCode + "-" + docNumber : docNumber;
			int fileType = doc[3] instanceof Number ? ((Number) doc[3]).intValue() : 0;
			String[] folders = isCn ? ApplicationConstant.creditNoteFolders : ApplicationConstant.invoiceFolders;
			if (fileType <= 0 || fileType >= folders.length) {
				failed.put(docNumber + " : no view file for VIEW_TYPE " + doc[3]);
				continue;
			}
			Path folderDir = cpgDir.resolve(folders[fileType]);
			logger.info("folderDir : "+folderDir.toAbsolutePath().toString());
			Path fileDir = folderDir;
			if (yearFolder) {
				cal.setTime((Date) doc[2]);
				fileDir = folderDir.resolve(String.valueOf(cal.get(Calendar.YEAR)));
			}
			Path file = fileDir.resolve(fileName + ApplicationConstant.filExtensions[fileType]).normalize();

			// file name must not move the path out of the view file folder
			if (!file.getParent().equals(fileDir)) {
				failed.put(docNumber + " : invalid file path " + file);
				continue;
			}
			try {
				logger.info("file : "+file.toAbsolutePath().toString());
				if (Files.deleteIfExists(file)) {
				//if (true) {
					deleted.put(file.toString());
				} else {
					notFound.put(file.toString());
				}
			} catch (Exception e) {
				logger.info(Utils.getStackTrace(e));
				failed.put(file + " : " + e.getMessage());
			}
		}

		logger.info("purgeViewFiles : docs " + docs.size() + ", deleted " + deleted.length() + ", notFound " + notFound.length() + ", failed " + failed.length());
		res.put("docCount", docs.size());
		res.put("delViewFileCount", deleted.length());
		res.put("deletedFiles", deleted);
		res.put("notFoundFiles", notFound);
		res.put("failedFiles", failed);
		return res;
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
