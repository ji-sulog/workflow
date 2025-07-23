package com.jzip.workflow.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalFormRequestDto {
    private String title;
    private String content;
    private Long writerId;
    private Long approverId;
    private String rmk;
}
