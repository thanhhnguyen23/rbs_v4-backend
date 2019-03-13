package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Book;
import com.revature.models.WishList;
import com.revature.util.ConnectionFactory;

public class WishListDAO {

	private static Logger log = LogManager.getLogger(WishListDAO.class);
	
	public WishList getUserWishList(int userId) {
		
		WishList userWishlist = new WishList(userId);
		
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			PreparedStatement pstmt = conn.prepareStatement(
					  "SELECT * FROM user_wishlists w "
					+ "JOIN books USING (book_id)"
					+ "WHERE w.user_id = ?");
			
			pstmt.setInt(1, userId);
			userWishlist.setUserWishList(mapResultSet(pstmt.executeQuery()));
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		if (userWishlist.getUserId() == 0 || userWishlist.getUserWishList() == null) return null;
		
		return userWishlist;
	}

	public WishList addBookToWishList(int userId, int bookId) {
		
		WishList userWishlist = new WishList();
		
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			
			conn.setAutoCommit(false);
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user_wishlists VALUES (?, ?)");
			pstmt.setInt(1, userId);
			pstmt.setInt(2, bookId);
			
			int rowsInserted = pstmt.executeUpdate();
			
			if (rowsInserted > 0) {
				
				conn.commit();
				
				pstmt = conn.prepareStatement(
						  "SELECT * FROM user_wishlists w "
						+ "JOIN books USING (book_id)"
						+ "WHERE w.user_id = ?");
				
				pstmt.setInt(1, userId);
				userWishlist.setUserId(userId);
				userWishlist.setUserWishList(mapResultSet(pstmt.executeQuery()));
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		if (userWishlist.getUserId() == 0 || userWishlist.getUserWishList() == null) return null;
		
		return userWishlist;
	}
	
	private List<Book> mapResultSet(ResultSet rs) throws SQLException {
		List<Book> wishlist = new ArrayList<>();
		while(rs.next()) {
			Book book = new Book();
			book.setId(rs.getInt("book_id"));
			book.setIsbn(rs.getString("isbn"));
			book.setTitle(rs.getString("title"));
			book.setAuthor(rs.getString("author"));
			book.setGenre(rs.getString("genre"));
			book.setPrice(rs.getDouble("price"));
			book.setNumberInStock(rs.getInt("stock_count"));
			wishlist.add(book);
		}
		return wishlist;
	}

}
