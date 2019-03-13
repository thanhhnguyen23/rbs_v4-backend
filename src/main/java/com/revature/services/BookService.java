package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.BookDAO;
import com.revature.models.Book;

public class BookService {

	private static Logger log = LogManager.getLogger(BookService.class);
	
	private BookDAO bookDao = new BookDAO();
	
	public List<Book> getAllBooks() {
		return bookDao.getAll();
	}

	public Book getBookById(int id) {
		if (id < 1) return null;
		return bookDao.getById(id);
	}

	public Book getBookByIsbn(String isbn) {
		
		if(isbn.equals("")) {
			log.info("No ISBN value provided.");
			return null;
		}
		
		return bookDao.getBookByIsbn(isbn);
	}

	public List<Book> getBooksByTitle(String title) {
		
		if(title.equals("")) {
			log.info("No title value provided.");
			return new ArrayList<>();
		}
		
		return bookDao.getBooksByTitle(title);
	}

	public List<Book> getBooksByAuthor(String author) {
		
		if(author.equals("")) {
			log.info("No author value provided.");
			return new ArrayList<>();
		}
		
		return bookDao.getBooksByAuthor(author);
	}

	public List<Book> getBooksByGenre(String genre) {
		
		if(genre.equals("")) {
			log.info("No genre value provided.");
			return new ArrayList<>();
		}
		
		return bookDao.getBooksByGenre(genre);
	}

	public Book addBook(Book newBook) {
		
		if (newBook.getIsbn().equals("") || newBook.getTitle().equals("") || newBook.getAuthor().equals("")
				|| newBook.getGenre().equals("") || newBook.getNumberInStock() <= 0) {
			log.info("New book object is missing required fields");
			return null;
		}

		return bookDao.add(newBook);
	}

	public Book updateBook(Book updatedBook) {

		if (updatedBook.getIsbn().equals("") || updatedBook.getTitle().equals("") || updatedBook.getAuthor().equals("")
				|| updatedBook.getGenre().equals("") || updatedBook.getNumberInStock() <= 0) {
			log.info("Updated book object is missing required fields");
			return null;
		}

		return bookDao.update(updatedBook);
	}

	public boolean deleteBook(int id) {
		return bookDao.delete(id);
	}
	
	public boolean addBookToWishList(Book book) {
		
		log.error("Missing implementation");
		log.warn("Book, " + book.getTitle() + "not added to wishlist");
		throw new UnsupportedOperationException("BookService.addBookToWishList() is being refactored");
		
	}
}
