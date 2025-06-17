package com.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.library.dto.BookDTO;
import com.library.entity.Book;
import com.library.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List getAllBooks() {
		// TODO Auto-generated method stub
		return bookRepository.findAll();
	}

	public Book getBookById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found"));
		return book;
	}

	public Book addBook(BookDTO bookDTO) {
		// TODO Auto-generated method stub

		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setAuthor(bookDTO.getAuthor());
		book.setIsbn(bookDTO.getIsbn());
		book.setIsAvailable(bookDTO.getIsAvailable());
		book.setQuantity(bookDTO.getQuantity());

		return bookRepository.save(book);
	}

	public Book updateBook(Long id, BookDTO bookDTO) {
		Book oldBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found"));
		oldBook.setTitle(bookDTO.getTitle());
		oldBook.setAuthor(bookDTO.getAuthor());
		oldBook.setIsbn(bookDTO.getIsbn());
		oldBook.setIsAvailable(bookDTO.getIsAvailable());
		oldBook.setQuantity(bookDTO.getQuantity());
		return bookRepository.save(oldBook);
	}

	public void deleteBook(Long id) {

		bookRepository.deleteById(id);
	}

}
