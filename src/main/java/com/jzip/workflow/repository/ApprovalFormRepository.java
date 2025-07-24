package com.jzip.workflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jzip.workflow.domain.form.ApprovalForm;


@Repository
public interface ApprovalFormRepository extends JpaRepository<ApprovalForm, Long> {

    // approverId만 뽑아오는 쿼리 메서드
    @Query("SELECT a.approverId FROM ApprovalForm a WHERE a.id = :formId")
    Optional<Long> findApproverIdByFormId(@Param("formId") Long formId);
    
}