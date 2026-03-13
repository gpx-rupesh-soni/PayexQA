package rcom.payex.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import rcom.payex.model.CPG;
import rcom.payex.model.CpgSettings;

/** DistributorDao */
public interface CPGDao {
	
	HashMap<String, String> cpgCodeToCpgTagMap = new HashMap<String, String>();

	/** Method - DistributorDao 
	 * @return List<Distributor>  */
	public List<CPG> getCPGList();
	
	public CPG getCPG(String payexCpgCode);
	
	/** Method - DistributorDao 
	 * @return List<SMSTemplate>  */
	public List<String> getCpgCodesByCpgSetting(String settingFor);
	
	/** Method - DistributorDao 
	 * @param id
	 * @return Distributor  */
	public CpgSettings getCpgSettings(String cpgCode);

	public static String getCpgTag(String cpgCode, Session session) {		
		if(!cpgCodeToCpgTagMap.containsKey(cpgCode)) {
			cpgCodeToCpgTagMap.put(cpgCode, session.createNativeQuery("select cpg_tag from cpg_master where payex_cpg_code = '"+cpgCode+"'").uniqueResult().toString());
		}		
		return cpgCodeToCpgTagMap.get(cpgCode);
	}

}
