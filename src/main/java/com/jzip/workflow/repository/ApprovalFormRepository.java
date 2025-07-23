package com.jzip.workflow.repository;

import com.jzip.workflow.domain.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalFormRepository extends JpaRepository<ApprovalForm, Long> {
    // 필요 시 커스텀 쿼리 추가 가능
}
