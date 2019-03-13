package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Role;
import com.revature.util.ConnectionFactory;

public class RoleDAO implements DAO<Role> {
	
	private static Logger log = LogManager.getLogger(RoleDAO.class);

	@Override
	public List<Role> getAll() {
		
		List<Role> roles = new ArrayList<>();
		
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {	
		
			ResultSet rs = conn.prepareStatement("SELECT * FROM user_roles").executeQuery();
			
			while(rs.next()) {
				roles.add(new Role(rs.getInt("role_id"), rs.getString("role_name")));
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return roles;
	}

	@Override
	public Role getById(int id) {
		Role role = new Role();
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user_roles WHERE role_id = ?");
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			role = this.mapResultSet(rs);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		if(role.getRoleId() == 0) return null;
		
		return role;
	}

	@Override
	public Role add(Role newRole) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			conn.setAutoCommit(false);
			
			String [] keys = new String[1];
			keys[0] = "role_id";
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user_roles VALUES (0, ?)", keys);
			pstmt.setString(1, newRole.getRoleName());
				
			int rowsInserted = pstmt.executeUpdate();
			
			if(rowsInserted > 0) {
				conn.commit();
				ResultSet rs = pstmt.getGeneratedKeys();
				newRole.setRoleId(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		if(newRole.getRoleId() == 0) return null;
		
		return newRole;
	}

	@Override
	public Role update(Role updatedRole) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			conn.setAutoCommit(false);
			
			PreparedStatement pstmt = conn.prepareStatement("UPDATE user_roles SET role_name = ? WHERE role_id = ?");
			pstmt.setString(1, updatedRole.getRoleName());
			pstmt.setInt(2, updatedRole.getRoleId());
			
			if(pstmt.executeUpdate() == 0) return null;
			
			conn.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return updatedRole;
	}

	@Override
	public boolean delete(int id) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user_roles WHERE role_id = ?");
			pstmt.setInt(1, id);
			
			if (pstmt.executeUpdate() == 0) return false;
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return true;
	}
	
	private Role mapResultSet(ResultSet rs) throws SQLException {
		Role role = new Role();
		role.setRoleId(rs.getInt("role_id"));
		role.setRoleName(rs.getString("role_name"));
		return role;
	}

}
