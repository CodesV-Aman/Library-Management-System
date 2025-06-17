package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.IssueRecord;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

}
