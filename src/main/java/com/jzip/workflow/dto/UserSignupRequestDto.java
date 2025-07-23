package com.jzip.workflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequestDto {
    private String username;
    private String password;
    private String role;  // ex: ROLE_USER, ROLE_APPROVER
}
