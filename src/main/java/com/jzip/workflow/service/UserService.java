package com.jzip.workflow.service;

import com.jzip.workflow.domain.user.User;
import com.jzip.workflow.dto.user.LoginRequestDto;
import com.jzip.workflow.dto.user.UserSignupRequestDto;
import com.jzip.workflow.repository.UserRepository;
import com.jzip.workflow.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public User signup(UserSignupRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // 🔐 비밀번호 암호화
                .role(dto.getRole())
                .build();

        return userRepository.save(user);
    }
    
    public String login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
