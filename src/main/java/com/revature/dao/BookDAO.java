package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Book;
import com.revature.util.ConnectionFactory;

import oracle.jdbc.internal.OracleTypes;

public class BookDAO implements DAO<Book> {
	
	private static Logger log = LogManager.getLogger(BookDAO.class);

	public List<Book> getBooksByTitle(String title) {
		List<Book> books = new ArrayList<>();

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE (title LIKE ?) OR (title LIKE ?)");
			pstmt.setString(1, "%" + title.toLowerCase() + "%");
			pstmt.setString(2, "%" + title.substring(0, 1).toUpperCase().concat(title.substring(1).toLowerCase()) + "%");

			ResultSet rs = pstmt.executeQuery();
			books = this.mapResultSet(rs);

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return books;
	}

	public List<Book> getBooksByAuthor(String author) {
		List<Book> books = new ArrayList<>();

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE (author LIKE ?) OR (author LIKE ?)");
			pstmt.setString(1, "%" + author.toLowerCase() + "%");
			pstmt.setString(2, "%" + author.substring(0, 1).toUpperCase().concat(author.substring(1).toLowerCase()) + "%");

			ResultSet rs = pstmt.executeQuery();
			books = this.mapResultSet(rs);

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return books;
	}

	public List<Book> getBooksByGenre(String genre) {
		List<Book> books = new ArrayList<>();

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE (genre LIKE ?) OR (genre LIKE ?)");
			pstmt.setString(1, "%" + genre.toLowerCase() + "%");
			pstmt.setString(2, "%" + genre.substring(0, 1).toUpperCase().concat(genre.substring(1).toLowerCase()) + "%");

			ResultSet rs = pstmt.executeQuery();
			books = this.mapResultSet(rs);

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return books;
	}

	public Book getBookByIsbn(String isbn) {
		Book book = new Book();

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE isbn = ?");
			pstmt.setString(1, isbn);

			ResultSet rs = pstmt.executeQuery();
			List<Book> books = this.mapResultSet(rs);
			if (books.isEmpty()) book = null;
			else book = books.get(0);

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return book;
	}

	@Override
	public List<Book> getAll() {

		List<Book> books = new ArrayList<>();

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			CallableStatement cstmt = conn.prepareCall("{CALL get_all_books(?)}");
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();

			ResultSet rs = (ResultSet) cstmt.getObject(1);
			books = this.mapResultSet(rs);

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return books;
	}

	@Override
	public Book getById(int bookId) {
		Book book = new Book();

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE book_id = ?");
			pstmt.setInt(1, bookId);

			ResultSet rs = pstmt.executeQuery();
			book = this.mapResultSet(rs).get(0);

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return book;
	}

	@Override
	public Book add(Book newBook) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			conn.setAutoCommit(false);

			String sql = "INSERT INTO books VALUES (0, ?, ?, ?, ?, ?)";
			String[] keys = new String[1];
			keys[0] = "book_id";

			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, newBook.getIsbn());
			pstmt.setString(2, newBook.getTitle());
			pstmt.setString(3, newBook.getAuthor());
			pstmt.setString(4, newBook.getGenre());
			pstmt.setInt(5, newBook.getNumberInStock());

			int rowsInserted = pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rowsInserted != 0) {

				while (rs.next()) {
					newBook.setId(rs.getInt(1));
				}

				conn.commit();

			}

		} catch (SQLIntegrityConstraintViolationException sicve) {
			log.warn("ISBN already exists.");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		if (newBook.getId() == 0) return null;

		return newBook;
	}

	@Override
	public Book update(Book updatedBook) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

			conn.setAutoCommit(false);

			String sql = "UPDATE users SET isbn = ?, title = ?, author = ?, genre = ?, stock_count = ? WHERE user_id = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updatedBook.getIsbn());
			pstmt.setString(2, updatedBook.getTitle());
			pstmt.setString(3, updatedBook.getAuthor());
			pstmt.setString(4, updatedBook.getGenre());
			pstmt.setInt(5, updatedBook.getNumberInStock());

			int rowsUpdated = pstmt.executeUpdate();

			if (rowsUpdated != 0) {
				conn.commit();
				return updatedBook;
			}

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return null;
	}

	@Override
	public boolean delete(int bookId) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM books WHERE book_id = ?");
			pstmt.setInt(1, bookId);
			
			if (pstmt.executeUpdate() == 0) return false;
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return true;
	}

	private List<Book> mapResultSet(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();

		while (rs.next()) {
			Book book = new Book();
			book.setId(rs.getInt("book_id"));
			book.setIsbn(rs.getString("isbn"));
			book.setTitle(rs.getString("title"));
			book.setAuthor(rs.getString("author"));
			book.setGenre(rs.getString("genre"));
			book.setPrice(rs.getDouble("price"));
			book.setNumberInStock(rs.getInt("stock_count"));
			books.add(book);
		}

		return books;
	}

}