package com.jzip.workflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // 테이블 이름 명시
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // 로그인용 ID

    @Column(nullable = false)
    private String password;  // 암호화된 비밀번호

    @Column(nullable = false)
    private String role;      // ex: ROLE_USER, ROLE_APPROVER, ROLE_ADMIN

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
