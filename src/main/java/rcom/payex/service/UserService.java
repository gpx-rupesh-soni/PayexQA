package rcom.payex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rcom.payex.dao.UserDao;
import rcom.payex.model.User;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	public void addUSer(User user) {
		userDao.addUSer(user);
	}

	@Transactional
	public User authUser(User user) {
		return userDao.authUser(user);
	}

	@Transactional
	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}	
}
