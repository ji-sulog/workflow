package com.jzip.workflow.service;

import com.jzip.workflow.domain.ApprovalForm;
import com.jzip.workflow.domain.ApprovalStatus;
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

    public ApprovalForm createForm(ApprovalForm form) {
        form.setStatus(ApprovalStatus.DRAFT);
        return formRepository.save(form);
    }

    public ApprovalForm getFormById(Long id) {
    return formRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));
    }

    public List<ApprovalForm> getAllForms() {
        return formRepository.findAll();
    }

    @Transactional
    public ApprovalForm lockForm(Long formId, Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (form.isLocked()) {
            throw new RuntimeException("이미 다른 사용자가 처리 중입니다");
        }

        form.setLocked(true);
        form.setLockedBy(userId);
        form.setLockedAt(LocalDateTime.now());
        return form;
    }

    @Transactional
    public ApprovalForm lockAndGetForm(Long formId, Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (form.isLocked()) {
            throw new RuntimeException("이미 다른 사용자가 처리 중입니다");
        }

        form.setLocked(true);
        form.setLockedBy(userId);
        form.setLockedAt(LocalDateTime.now());

        return form;
    }

    @Transactional
    public void unlockForm(Long formId, Long userId) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (!form.isLocked() || !form.getLockedBy().equals(userId)) {
            throw new RuntimeException("잠금 해제 권한이 없습니다");
        }

        form.setLocked(false);
        form.setLockedBy(null);
        form.setLockedAt(null);
    }

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

    @Transactional
    public ApprovalForm approveForm(Long formId, String memo) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));

        if (!form.getStatus().equals(ApprovalStatus.SUBMITTED)) {
            throw new RuntimeException("SUBMITTED 상태에서만 승인할 수 있습니다");
        }
        // if (!form.getApproverId().equals(userId)) {
        //     throw new RuntimeException("승인자만 승인할 수 있습니다");
        // }
        form.setStatus(ApprovalStatus.APPROVED);
        form.setMemo(memo);
        form.setLocked(false);
        form.setLockedBy(null); //이건뭐지???
        return form;
    }

    @Transactional
    public ApprovalForm rejectForm(Long formId, String memo) {
        ApprovalForm form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다"));
    
        if (!form.getStatus().equals(ApprovalStatus.SUBMITTED)) {
            throw new RuntimeException("SUBMITTED 상태에서만 반려할 수 있습니다");
        }
        // if (!form.getApproverId().equals(userId)) {
        //     throw new RuntimeException("승인자만 반려할 수 있습니다");
        // }

        form.setStatus(ApprovalStatus.REJECTED);
        form.setMemo(memo);
        form.setLocked(false);
        form.setLockedBy(null);
        return form;
    }
}
