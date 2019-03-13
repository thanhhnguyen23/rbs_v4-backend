package com.revature.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.UserDAO;
import com.revature.dao.WishListDAO;
import com.revature.models.User;

public class UserService {

	private static Logger log = LogManager.getLogger(UserService.class);
	
	private UserDAO userDao = new UserDAO();
	private WishListDAO wishlistDao = new WishListDAO();

	public List<User> getAllUsers() {
		return userDao.getAll();
	}

	public User getUserById(int userId) {
		return userDao.getById(userId);
	}

	public User getUserByUsername(String username) {
		return userDao.getByUsername(username);
	}

	public User getUserByCredentials(String username, String password) {

		User user = null;
		
		// Verify that neither of the credentials are empty string
		if (!username.equals("") && !password.equals("")) {
			user = userDao.getByCredentials(username, password);
			if(user != null) user.setWishlist(wishlistDao.getUserWishList(user.getId()).getUserWishList());
			return user;
		}

		log.info("Empty username and/or password value(s) provided"); 
		return null;
	}

	public User addUser(User newUser) {

		// Verify that there are no empty fields
		if (newUser.getUsername().equals("") || newUser.getPassword().equals("") || newUser.getFirstName().equals("")
				|| newUser.getLastName().equals("")) {
			log.info("New user object is missing required fields");
			return null;
		}

		return userDao.add(newUser);
	}

	public User updateUser(User updatedUser) {

		// Verify that there are no empty fields
		if (updatedUser.getUsername().equals("") || updatedUser.getPassword().equals("")
				|| updatedUser.getFirstName().equals("") || updatedUser.getLastName().equals("")) {
			log.info("Updated user object is missing required fields");
			return null;
		}
		
		// Attempt to persist the user to the dataset
		User persistedUser = userDao.update(updatedUser);
		

		// If the update attempt was successful, update the currentUser of AppState, and return the updatedUser
		if (persistedUser != null) {
			return updatedUser;
		}

		// If the update attempt was unsuccessful, return null
		log.warn("User update failed");
		return null;

	}

	public boolean deleteUser(int userId) {
		return userDao.delete(userId);
	}
}
