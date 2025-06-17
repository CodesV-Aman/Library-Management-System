package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.IssueRecord;
import com.library.repository.IssueRecordRepository;
import com.library.services.IssueRecordService;

@RestController
@RequestMapping("/issuerecord")
public class issueRecordController {

	@Autowired
	private IssueRecordService issueRecordService;

	@PostMapping("/issuethebook/{bookid}")
	public ResponseEntity<IssueRecord> issueBook(@PathVariable Long bookid) {

		return ResponseEntity.ok(issueRecordService.issueTheBook(bookid));

	}

	@PostMapping("/returnthebook/{issuerecordid}")
	public ResponseEntity<IssueRecord> returnTheBook(@PathVariable Long issuerecordid) {

		return ResponseEntity.ok(issueRecordService.returnTheBook(issuerecordid));

	}

}
