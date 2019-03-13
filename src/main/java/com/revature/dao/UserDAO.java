package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

/**
 * Acts as a data access object for interacting with the USERS table of the relational DB.
 * 
 * @author Wezley
 * 
 */
public class UserDAO implements DAO<User> {
	
	private static Logger log = LogManager.getLogger(UserDAO.class);
	
	/**
	 * Queries the DB for a user with the provided username.
	 * 
	 * @param username
	 * 		The username (unique in the DB) of the sought user.
	 * 
	 * @return 
	 * 		A retrieved user from the DB with a username matching the one provided. Could
	 * 		return null if no user was found with the given username.
	 */
	public User getByUsername(String username) {
		
		User user = null;
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users JOIN user_roles USING (role_id) WHERE username = ?");
			pstmt.setString(1, username);
			
			List<User> users = this.mapResultSet(pstmt.executeQuery());
			if (!users.isEmpty()) user = users.get(0);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return user;
	}
	
	/**
	 * Queries the DB for a user with the provided credentials.
	 * 
	 * @param username
	 * 		The username (unique in the DB) of the sought user.
	 * 
	 * @param password
	 * 		The password of the sought user.
	 * 	
	 * @return
	 * 		A retrieved user from the DB with credentials matching those provided. Could
	 * 		return null if no user was found with the given credentials.
	 */
	public User getByCredentials(String username, String password) {
		
		User user = null;
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users JOIN user_roles USING (role_id) WHERE username = ? AND password = ?");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			List<User> users = this.mapResultSet(pstmt.executeQuery());
			if (!users.isEmpty()) user = users.get(0);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
				
		return user;
	}
	
	@Override
	public List<User> getAll() {
		
		List<User> users = new ArrayList<>();
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users JOIN user_roles USING (role_id)");
			users = this.mapResultSet(rs);
			
			// Hide all user passwords for security purposes
			users.forEach(u -> u.setPassword("*********"));
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return users;
	}
	
	@Override
	public User getById(int userId) {
		
		User user = null;
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users JOIN user_roles USING (role_id) WHERE user_id = ?");
			pstmt.setInt(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			List<User> users = this.mapResultSet(rs);
			
			if (!users.isEmpty()) {
				user = users.get(0);
				user.setPassword("*********");
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return user;
	}

	@Override
	public User add(User newUser) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			conn.setAutoCommit(false);
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users VALUES (0, ?, ?, ?, ?, 3)", new String[] {"user_id"});
			pstmt.setString(1, newUser.getUsername());
			pstmt.setString(2, newUser.getPassword());
			pstmt.setString(3, newUser.getFirstName());
			pstmt.setString(4, newUser.getLastName());
			
			if(pstmt.executeUpdate() != 0) {
				
				// Retrieve the generated primary key for the newly added user
				ResultSet rs = pstmt.getGeneratedKeys();
				
				// The newly added user will need a non-null wishlist
				newUser.setWishlist(new ArrayList<>());
				
				while(rs.next()) {
					newUser.setId(rs.getInt(1));
					newUser.setRole(new Role(3));
				}
				
				conn.commit();
				
			}
					
		} catch (SQLIntegrityConstraintViolationException sicve) {
			log.error(sicve.getMessage());
			log.warn("Username already taken.");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		if(newUser.getId() == 0) return null;
		
		return newUser;
	}

	@Override
	public User update(User updatedUser) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			conn.setAutoCommit(false);
			
			String sql = "UPDATE users SET password = ?, first_name = ?, last_name = ? WHERE user_id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updatedUser.getPassword());
			pstmt.setString(2, updatedUser.getFirstName());
			pstmt.setString(3, updatedUser.getLastName());
			pstmt.setInt(4, updatedUser.getId());
			
			if(pstmt.executeUpdate() != 0) {
				conn.commit();
				return updatedUser;
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return null;
	}

	@Override
	public boolean delete(int userId) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			conn.setAutoCommit(false);
			
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
			pstmt.setInt(1, userId);
			
			if (pstmt.executeUpdate() > 0) {
				conn.commit();
				return true;
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return false;
		
	}
	
	/**
	 * Maps the records within a ResultSet object to a list of users.
	 * 
	 * @param rs
	 * 		The ResultSet object obtained from some SQL statement execution.
	 *  
	 * @return
	 * 		A list of users (zero or more) which are instantiated using the data from the ResultSet.
	 * 
	 * @throws SQLException
	 * 		Invoking methods on the ResultSet object could result in SQLException, callers of
	 * 		this will be required to handle it.
	 */
	private List<User> mapResultSet(ResultSet rs) throws SQLException {
		
		List<User> users = new ArrayList<>();
		
		while(rs.next()) {
			User user = new User();
			user.setId(rs.getInt("user_id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setRole(new Role(rs.getInt("role_id")));
			users.add(user);
		}
		
		return users;
	}

}
