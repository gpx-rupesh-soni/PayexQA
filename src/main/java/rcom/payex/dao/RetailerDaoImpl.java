package rcom.payex.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rcom.payex.model.CPG;
import rcom.payex.model.Retailer;
import rcom.payex.utils.Utils;

@Repository
public class RetailerDaoImpl implements RetailerDao {

	@Autowired
	private SessionFactory sessionFactory;

	Logger logger = LogManager.getLogger(RetailerDaoImpl.class);
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Retailer getRetailer(String payexRetailerCode) {
		return getSession().get(Retailer.class, payexRetailerCode);
	}
	
	@Override
	public List<String> getPayexRetailerCodeByDistRetMap(String cpgRetailerCode, String cpgTag) {
		
		String sqlRetDistMap = "select PAYEX_RETAILER_CODE from ret_dist_map_"+cpgTag+" where CPG_RETAILER_CODE =:payexCpgCode"; 
		
		NativeQuery query = getSession().createNativeQuery(sqlRetDistMap);		
		List<String> res = query.setParameter("payexCpgCode",cpgRetailerCode).list();
		return res;
		
	}

	@Override
	public Retailer getRetailer(String mobile, String password) {
		Criteria criteria = getSession().createCriteria(Retailer.class);
		criteria.add(Restrictions.eq("testData", 1));
		criteria.add(Restrictions.eq("mobile_NUMBER", mobile));
		criteria.add(Restrictions.eq("pin", password));
		List<Retailer> data = criteria.list();
		if (!data.isEmpty())
			return data.get(0);
		else
			return null;
	}
	
	public List<String> getRetailerCpg(String payexRetailerCode, List<CPG> cpgs){
		
		StringBuilder queryStr = new StringBuilder("select payex_cpg_code from CPG_DISTRIBUTOR_MAPPING where payex_distributor_code in (");
		boolean first = true;
		for (CPG cpg : cpgs) {
			queryStr.append((first ? "" : " UNION ") + "select payex_distributor_code from ret_dist_map_"
					+ cpg.getCPG_TAG() + " where payex_retailer_code = '" + payexRetailerCode + "'");
			first = false;
		}
		queryStr.append(")");
		
		NativeQuery query = getSession().createNativeQuery(queryStr.toString());		
		List<String> res = query.list();
		return res;
	}
	
	@Override
	public void updateRetailer(Retailer retailer, String type,int serverMode) {
		switch (type) {
		case "mobile":
			if(serverMode==0)
				updateRetailerMobile(retailer);
			else if(serverMode==1)
				updateRetailerMobileForQA(retailer);
			break;

		case "info":
			updateRetailerInfo(retailer);
			break;

		case "pin":
			updateRetailerPin(retailer);
			break;
		default:
			break;
		}

	}
	
	@Override
	public void updateRetailerMobile(Retailer retailer) {
		Query query;
		Session session = getSession();

		String sql = "update Retailer set mobile_NUMBER=:mobile_NUMBER where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE and pin is null";
		query = session.createQuery(sql);
		query.setParameter("mobile_NUMBER", retailer.getMobile_NUMBER())
		.setParameter("PAYEX_RETAILER_CODE",
				retailer.getPayex_RETAILER_CODE()).executeUpdate();
	}
	
	@Override
	public void updateRetailerMobileForQA(Retailer retailer) {
		Query query;
		Session session = getSession();

		String sql = "update Retailer set mobile_NUMBER=:mobile_NUMBER where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE";
		query = session.createQuery(sql);
		query.setParameter("mobile_NUMBER", retailer.getMobile_NUMBER())
		.setParameter("PAYEX_RETAILER_CODE",
				retailer.getPayex_RETAILER_CODE()).executeUpdate();
	}

	@Override
	public void updateRetailerPin(Retailer retailer) {
		Query query;
		Session session = getSession();

		String sql = "update Retailer set pin=:pin where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE and pin is null";
		query = session.createQuery(sql);
		query.setParameter("pin", retailer.getPin()).setParameter("PAYEX_RETAILER_CODE",retailer.getPayex_RETAILER_CODE()).executeUpdate();
	}
	
	@Override
	public void updateRetailerInfo(Retailer retailer) {
		int isFirst = 0;
		HashMap<String, String> data = new HashMap<>();
		Query query;
		Session session = getSession();

		String sql = "update Retailer set ";

		if (!retailer.getKey_PERSON().isEmpty()) {
			sql = sql + (isFirst > 0 ? "," : "") + "key_PERSON=:key_PERSON";
			data.put("key_PERSON", retailer.getKey_PERSON());
			isFirst++;
		}

		if (!retailer.getEmail_ID().isEmpty()) {
			sql = sql + (isFirst > 0 ? "," : "") + "email_ID=:email_ID";
			data.put("email_ID", retailer.getEmail_ID());
			isFirst++;
		}

		if (!retailer.getAddress().isEmpty()) {
			sql = sql + (isFirst > 0 ? "," : "") + "address=:address";
			data.put("address", retailer.getAddress());
			isFirst++;
		}

		sql = sql + " where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE";

		query = session.createQuery(sql);

		for (Map.Entry<String, String> entry : data.entrySet()) {
			String key = entry.getKey();
			query.setParameter(key, entry.getValue());

		}

		query.setParameter("PAYEX_RETAILER_CODE",
				retailer.getPayex_RETAILER_CODE());
		query.executeUpdate();

	}
	
	@Override
	public JSONObject deleteRetailer(Retailer retailer, List<String> payexCpgCodes) {
		
		System.out.println("deleteRetailer called...");
		System.out.println("payexRetailerCode : "+retailer.getPayex_RETAILER_CODE());
		System.out.println("payexCpgCode : "+payexCpgCodes);
		
		NativeQuery query;
		Session session = getSession();
		
		JSONObject res = new JSONObject();

		String sqlDelRetSetting = "delete from retailer_setting where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE"; 
		String sqlDelRet = "delete from retailer_master where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE";		
		
		try {
			query = session.createNativeQuery(sqlDelRetSetting);		
			res.put("delRetSettingCount", query.setParameter("PAYEX_RETAILER_CODE",retailer.getPayex_RETAILER_CODE()).executeUpdate());
			
			for (String payexCpgCode : payexCpgCodes) {
				String sqlDelRetMap = "delete from ret_dist_map_"+(session.createNativeQuery("select CPG_TAG from CPG_MASTER where PAYEX_CPG_CODE=:payexCpgCode").setParameter("payexCpgCode",payexCpgCode).uniqueResult())+" where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE";
				System.out.println("sqlDelRetMap : "+sqlDelRetMap);
				query = session.createNativeQuery(sqlDelRetMap);		
				res.put("delRetMapCount", query.setParameter("PAYEX_RETAILER_CODE",retailer.getPayex_RETAILER_CODE()).executeUpdate());
				
				if("CPGBRGSTN".equalsIgnoreCase(payexCpgCode)) {
					String sqlDelBSIDLOBMap = "delete from DISTRIBUTOR_DEALER_DIV_MAPPING where payex_RETAILER_CODE =:PAYEX_RETAILER_CODE";
					query = session.createNativeQuery(sqlDelBSIDLOBMap);		
					res.put("delRetLOBMapCount", query.setParameter("PAYEX_RETAILER_CODE",retailer.getPayex_RETAILER_CODE()).executeUpdate());
				}
			}			
			
			query = session.createNativeQuery(sqlDelRet);		
			res.put("delRetCount", query.setParameter("PAYEX_RETAILER_CODE",retailer.getPayex_RETAILER_CODE()).executeUpdate());			
		
		}catch (Exception e) {
			logger.info(Utils.getStackTrace(e));
			try {
				res.put("error","Failed due to exception");
			} catch (JSONException e1) {
				logger.info(Utils.getStackTrace(e));
			}
		}
		
		return res;
	}
	
	@Override
	public Map<String, Set<String>> getMissingRetailers(Map<String, Set<String>> inputMap) {

	    Map<String, Set<String>> result = new HashMap<>();

	    for (Map.Entry<String, Set<String>> entry : inputMap.entrySet()) {

	        String tableName = CPGDao.getCpgTag(entry.getKey(), getSession()) ;
	        logger.info("tableName : "+tableName);
	        tableName = "ret_dist_map_"+tableName;
	        logger.info("tableName : "+tableName);
	        Set<String> retailerCodes = entry.getValue();

	        if (retailerCodes.isEmpty()) {
	            continue;
	        }

	        String sql =
	            "SELECT CPG_RETAILER_CODE " +
	            "FROM " + tableName +
	            " WHERE CPG_RETAILER_CODE IN (:retailers)";

	        @SuppressWarnings("unchecked")
	        List<String> existingRetailers = getSession().createNativeQuery(sql)
	                .setParameter("retailers", retailerCodes)
	                .getResultList();

	        Set<String> missing = new HashSet<>(retailerCodes);
	        missing.removeAll(existingRetailers);

	        if (!missing.isEmpty()) {
	            result.put(entry.getKey(), missing);
	        }
	    }

	    return result;
	}

	
}

	
