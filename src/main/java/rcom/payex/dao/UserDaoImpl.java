package rcom.payex.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import rcom.payex.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	String dateformat = "DD-MM-YYYY";

	private static Logger logger = LogManager.getLogger(UserDaoImpl.class);
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void addUSer(User user) {
		getSession().save(user);
	}

	//@author RahulB 03-06-2021 T4522 Console based offline cf
	@Override
	public User authUser(User user) {
		Session session = getSession();

		@SuppressWarnings("unchecked")
		List<User> users = session.createCriteria(User.class)
				.add(Restrictions.eq("userid", user.getUserid()))
				.add(Restrictions.eq("password", user.getPassword())).list();

		if (users.size() == 1) {
			return users.get(0);
		}
		return null;
	}

	@Override
	public void deleteUser(User user) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.delete(user);
		tx.commit();

	}	
}
