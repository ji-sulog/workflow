package com.jzip.workflow.controller;

import com.jzip.workflow.domain.ApprovalForm;
import com.jzip.workflow.domain.ApprovalStatus;
import com.jzip.workflow.dto.ApprovalFormRequestDto;
import com.jzip.workflow.service.ApprovalFormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class ApprovalFormController {

    private final ApprovalFormService formService;

    // 신청서 작성
    // @PostMapping
    // public ApprovalForm createForm(@RequestBody ApprovalForm form) {
    //     return formService.createForm(form);
    // }
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
    @GetMapping("/{id}")
    @Operation(summary = "신청서 단건 조회", description = "ID로 신청서 상세 정보를 조회합니다.")
    public ApprovalForm getForm(@PathVariable Long id) {
        return formService.getFormById(id);
    }
    @GetMapping
    @Operation(summary = "신청서 전체 목록 조회", description = "모든 신청서 목록을 조회합니다.")
    public List<ApprovalForm> getAllForms() {
        return formService.getAllForms();
    }

    // 잠금
    @PostMapping("/{id}/lock")
    @Operation(summary = "신청서 잠금", description = "다른 사용자가 동시에 접근하지 못하도록 신청서를 잠금 처리합니다.")
    public ApprovalForm lockForm(@PathVariable Long id, @RequestParam Long userId) {
        return formService.lockForm(id, userId);
    }

    @GetMapping("/{id}/locked")
    @Operation(summary = "신청서 조회 + 잠금", description = "신청서를 조회하면서 동시에 잠금 처리합니다.")
    public ApprovalForm lockAndGetForm(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return formService.lockAndGetForm(id, userId);
    }

    // 잠금 해제
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
    @PostMapping("/{id}/approve")
    @Operation(summary = "신청서 승인", description = "승인자가 신청서를 승인합니다.")
    public ApprovalForm approve(@PathVariable Long id, @RequestParam String memo) {
        //TODO 승인자 검색 > return formService.approveForm(id, apvId, memo);

        return formService.approveForm(id, memo);
    }

    // 반려
    @PostMapping("/{id}/reject")
    @Operation(summary = "신청서 반려", description = "승인자가 신청서를 반려합니다.")
    public ApprovalForm reject(@PathVariable Long id, @RequestParam String memo) {
        //TODO 승인자 검색 > return formService.approveForm(id, apvId, memo);
        return formService.rejectForm(id, memo);
    }
}
