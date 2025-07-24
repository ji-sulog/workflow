package com.jzip.workflow.dto.form;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalFormRequestDto {

    @Schema(description = "신청서 제목", example = "휴가 신청서")
    private String title;

    @Schema(description = "신청서 본문", example = "2025년 8월 10일 ~ 8월 20일까지 휴가 신청합니다.")
    private String content;

    @Schema(description = "작성자 ID", example = "1")
    private Long writerId;

    @Schema(description = "승인자 ID", example = "2")
    private Long approverId;

    @Schema(description = "비고", example = "연차 소진 5일")
    private String rmk;
}
