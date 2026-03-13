package rcom.payex.dao;

import rcom.payex.model.User;


/** UserDao */
public interface UserDao {

	/** Method - UserDao 
	 * @param user
	 * @return void  */
	public void addUSer(User user);

	/** Method - UserDao 
	 * @param user
	 * @return User  */
	public User authUser(User user);

	/** Method - UserDao 
	 * @param user
	 * @return void  */
	public void deleteUser(User user);	
}
