package com.library.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.library.entity.Book;
import com.library.entity.IssueRecord;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.UserRepository;

@Service
public class IssueRecordService {

	@Autowired
	private IssueRecordRepository issueRecordRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	public IssueRecord issueTheBook(Long bookid) {
		Book book = bookRepository.findById(bookid).orElseThrow(() -> new RuntimeException("Book Not Found"));
		if (book.getQuantity() <= 0 || !book.getIsAvailable()) {
			throw new RuntimeException("Book is not available");
		}

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		IssueRecord issueRecord = new IssueRecord();
		issueRecord.setIssueDate(LocalDate.now());
		issueRecord.setDueDate(LocalDate.now().plusDays(14));
		issueRecord.setIsReturned(false);
		issueRecord.setUser(user);
		issueRecord.setBook(book);

		book.setQuantity(book.getQuantity() - 1);
		if (book.getQuantity() == 0) {
			book.setIsAvailable(false);
		}

		bookRepository.save(book);
		return issueRecordRepository.save(issueRecord);

	}

	public IssueRecord returnTheBook(Long issuerecordid) {
		IssueRecord issueRecord = issueRecordRepository.findById(issuerecordid)
				.orElseThrow(() -> new RuntimeException("Issue Record not found"));
		
		if(issueRecord.getIsReturned()) {
			throw new RuntimeException("Book is already returned");
		}

		Book book=issueRecord.getBook();
		book.setQuantity(book.getQuantity()+1);
		book.setIsAvailable(true);
		bookRepository.save(book);
		
		issueRecord.setReturnDate(LocalDate.now());
		issueRecord.setIsReturned(true);
		
		return issueRecordRepository.save(issueRecord);
	}

}
