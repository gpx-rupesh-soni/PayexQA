package rcom.payex.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import rcom.payex.model.CPG;
import rcom.payex.model.Retailer;

/** RetailerDao */
public interface RetailerDao {

	public Retailer getRetailer(String payexRetailerCode);
	public List<String> getPayexRetailerCodeByDistRetMap(String cpgRetailerCode, String cpgTag);
	public List<String> getRetailerCpg(String payexRetailerCode, List<CPG> cpgs);
	public void updateRetailer(Retailer retailer, String type,int serverMode);	
	public Retailer getRetailer(String username, String password);
	void updateRetailerInfo(Retailer retailer);
	void updateRetailerPin(Retailer retailer);
	void updateRetailerMobile(Retailer retailer);
	void updateRetailerMobileForQA(Retailer retailer);
	JSONObject deleteRetailer(Retailer retailer,  List<String> cpgTag);	
	public Map<String, Set<String>> getMissingRetailers(Map<String, Set<String>> inputMap);
	
}
