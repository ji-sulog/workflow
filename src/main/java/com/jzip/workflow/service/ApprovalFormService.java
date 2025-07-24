package com.jzip.workflow.service;

import com.jzip.workflow.domain.form.ApprovalForm;
import com.jzip.workflow.domain.form.ApprovalStatus;
import com.jzip.workflow.repository.ApprovalFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalFormService {

    private final ApprovalFormRepository formRepository;
    
    // 신청서 생성
    public ApprovalForm createForm(ApprovalForm form) {
        form.setStatus(ApprovalStatus.DRAFT);
        return formRepository.save(form);
    }

    // 신청서 전체 목록 조회
    public List<ApprovalForm> getAllForms() {
        return formRepository.findAll();
    }

    // 신청서 잠금 및 조회
    @Transactional
    public ApprovalForm lockAndGetForm(Long formId, Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (form.isLocked()) {
            throw new RuntimeException("이미 다른 사용자가 처리 중입니다");
        }
        
        if(form.getApproverId().equals(userId) && !form.getStatus().equals(ApprovalStatus.DRAFT)) {
            // 승인자일 경우 잠금 처리
            form.setLocked(true);
            form.setLockedBy(userId);
            form.setLockedAt(LocalDateTime.now());
        }

        return form;
    }
    // 신청서 잠금 해제
    @Transactional
    public void unlockForm(Long formId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));
        
        if (form.isLocked()) {
            form.setLocked(false);
            form.setLockedBy(null);
            form.setLockedAt(null);
        }
    }

    // 신청서 제출
    @Transactional
    public ApprovalForm submitForm(Long formId , Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (!form.getStatus().equals(ApprovalStatus.DRAFT)) {
            throw new RuntimeException("DRAFT 상태에서만 제출할 수 있습니다");
        }
        if (!form.getWriterId().equals(userId)) {
            throw new RuntimeException("작성자만 제출 할 수 있습니다");
        }

        form.setStatus(ApprovalStatus.SUBMITTED);
        return form;
    }

    // 신청서 승인/반려 :승인자만 가능

    @Transactional
    public ApprovalForm approveForm(Long formId, String memo, Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (!form.getStatus().equals(ApprovalStatus.SUBMITTED)) {
            throw new RuntimeException("SUBMITTED 상태에서만 승인할 수 있습니다");
        }
        if (!form.getApproverId().equals(userId)) {
            throw new RuntimeException("승인자만 승인할 수 있습니다");
        }
        form.setStatus(ApprovalStatus.APPROVED);
        form.setMemo(memo);

        // 잠금 해제 조건: 잠금 상태이거나, 승인자 본인이면 해제
        if (form.isLocked() || form.getApproverId().equals(userId)){
            form.setLocked(false);
            form.setLockedBy(null);
            form.setLockedAt(null);
        }
        return form;
    }

    @Transactional
    public ApprovalForm rejectForm(Long formId, String memo, Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));
    
        if (!form.getStatus().equals(ApprovalStatus.SUBMITTED)) {
            throw new RuntimeException("SUBMITTED 상태에서만 반려할 수 있습니다");
        }
        if (!form.getApproverId().equals(userId)) {
            throw new RuntimeException("승인자만 반려할 수 있습니다");
        }

        form.setStatus(ApprovalStatus.REJECTED);
        form.setMemo(memo);
        
        // 잠금 해제 조건: 잠금 상태이거나, 승인자 본인이면 해제
        if (form.isLocked() || form.getApproverId().equals(userId)){
            form.setLocked(false);
            form.setLockedBy(null);
            form.setLockedAt(null);
        }
        return form;
    }
}
