package rcom.payex.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rcom.payex.model.CPG;
import rcom.payex.model.CpgSettings;

@Repository
public class CPGDaoImpl implements CPGDao {
	
	List<CPG> cpgs;
	
	@Autowired
	private SessionFactory sessionFactory;
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}	

	@Override
	public List<CPG> getCPGList() {
		return getSession().createCriteria(CPG.class).list();
	}
	
	@Override
	public CPG getCPG(String payexCpgCode) {
		return getSession().get(CPG.class, payexCpgCode);	
	}

	@Override
	public List<String> getCpgCodesByCpgSetting(String settingFor) {
		String sql = "select PAYEX_CPG_CODE from CPG_SETTINGS where "+settingFor+"=1";
		List<String> codes = getSession().createSQLQuery(sql)
		        .list();
		
		return codes;
		
	}

	@Override
	public CpgSettings getCpgSettings(String payexCpgCode) {
		return getSession().get(CpgSettings.class, payexCpgCode);		
	}
}
