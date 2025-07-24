package com.jzip.workflow.domain.form;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private Long writerId;     // 사용자 ID
    private Long approverId;   // 승인자 ID

    @Lob
    private String memo;       // 승인/반려 의견

    @Lob
    private String rmk;        // 비고 (remark)

    private boolean isLocked;       // 조회 중 여부
    private Long lockedBy;          // 누가 락 걸었는지
    private LocalDateTime lockedAt; // 언제 락 걸었는지

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
