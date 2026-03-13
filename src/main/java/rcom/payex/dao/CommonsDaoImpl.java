package rcom.payex.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rcom.payex.model.CPG;
import rcom.payex.model.CPGDistributorMapping;
import rcom.payex.model.Distributor;

@Repository
public class CommonsDaoImpl implements CommonsDao {
	
	
	Logger logger = LogManager.getLogger(CommonsDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public CPG getCpg(String payexCpgCode) {
		try {
			Criteria criteria = getSession().createCriteria(CPG.class).add(Restrictions.eq("PAYEX_CPG_CODE", payexCpgCode));

			return (CPG) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public List<CPG> getCpgs(List<String> payexCpgCode) {
		try {
			Criteria criteria = getSession().createCriteria(CPG.class).add(Restrictions.in("PAYEX_CPG_CODE", payexCpgCode));

			return criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public Distributor getDistributor(String payexDistributorCode) {
		try {
			Criteria criteria = getSession().createCriteria(Distributor.class).add(Restrictions.eq("PAYEX_DISTRIBUTOR_CODE", payexDistributorCode));

			return (Distributor) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public CPGDistributorMapping getCPGDistributorMapping(String payexCpgCode) {
		try {
			Criteria criteria = getSession().createCriteria(CPGDistributorMapping.class).add(Restrictions.eq("PAYEX_DISTRIBUTOR_CODE", payexCpgCode));

			return (CPGDistributorMapping) criteria.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
