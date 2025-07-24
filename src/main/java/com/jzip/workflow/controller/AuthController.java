package com.jzip.workflow.controller;

import com.jzip.workflow.domain.user.User;
import com.jzip.workflow.dto.user.LoginRequestDto;
import com.jzip.workflow.dto.user.UserSignupRequestDto;
import com.jzip.workflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "사용자를 등록합니다.")
    public User signup(@RequestBody UserSignupRequestDto dto) {
        return userService.signup(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "ID와 비밀번호로 로그인 후 JWT 토큰을 발급받습니다.")
    public String login(@RequestBody LoginRequestDto dto) {
        return userService.login(dto); // 👉 로그인 처리 로직은 UserService에서 수행
    }

    @GetMapping("/userList")
    @Operation(summary = "사용자 목록 조회", description = "모든 사용자의 정보를 조회합니다.")
    public List<User> getUserList() {
        return userService.getUserList();
    }

}
