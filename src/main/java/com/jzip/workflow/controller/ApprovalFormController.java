package com.jzip.workflow.controller;

import com.jzip.workflow.domain.form.ApprovalForm;
import com.jzip.workflow.domain.form.ApprovalStatus;
import com.jzip.workflow.dto.form.ApprovalFormRequestDto;
import com.jzip.workflow.service.ApprovalFormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@EnableMethodSecurity(prePostEnabled = true)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class ApprovalFormController {

    private final ApprovalFormService formService;

    @PostMapping
    @Operation(summary = "신청서 생성", description = "새로운 신청서를 생성합니다.")
    public ApprovalForm createForm(@RequestBody ApprovalFormRequestDto requestDto) {
        ApprovalForm form = ApprovalForm.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .writerId(requestDto.getWriterId())
                .approverId(requestDto.getApproverId())
                .rmk(requestDto.getRmk())
                .status(ApprovalStatus.DRAFT)
                .isLocked(false)
                .build();

        return formService.createForm(form);
    }

    // 조회
    @GetMapping("/list")
    @Operation(summary = "신청서 전체 목록 조회", description = "모든 신청서 목록을 조회합니다.")
    public List<ApprovalForm> getAllForms() {
        return formService.getAllForms();
    }
    // +잠금
    @GetMapping("/{id}/locked")
    @Operation(summary = "신청서 조회 + 승인자 조회 시 잠금", description = "신청서를 조회하면서 동시에 잠금 처리합니다.")
    public ApprovalForm lockAndGetForm(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return formService.lockAndGetForm(id, userId);
    }

    //잠금 해제
    @PostMapping("/{id}/unlock")
    @Operation(summary = "신청서 잠금 해제", description = "사용자가 처리 중인 신청서의 잠금을 해제합니다.")
    public void unlockForm(@PathVariable Long id, @RequestParam Long userId) {
        formService.unlockForm(id, userId);
    }

    // 제출
    @PostMapping("/{id}/submit")
    @Operation(summary = "신청서 제출", description = "작성된 신청서를 제출합니다. 상태는 DRAFT → SUBMITTED로 변경됩니다.")
    public ApprovalForm submit(@PathVariable Long id, @RequestParam Long userId) {
        return formService.submitForm(id,userId);
    }

    // 승인
    @PreAuthorize("hasRole('APPROVER')")
    @PostMapping("/{id}/approve")
    @Operation(summary = "신청서 승인", description = "승인자가 신청서를 승인합니다.")
    public ApprovalForm approve(@PathVariable Long id, @RequestParam String memo, @RequestParam Long userId) {
        return formService.approveForm(id, memo, userId);
    }

    // 반려
    @PreAuthorize("hasRole('APPROVER')")
    @PostMapping("/{id}/reject")
    @Operation(summary = "신청서 반려", description = "승인자가 신청서를 반려합니다.")
    public ApprovalForm reject(@PathVariable Long id, @RequestParam String memo, @RequestParam Long userId) {
        return formService.rejectForm(id, memo, userId);
    }
}
