package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Dto.SignInDto;
import com.booktree.booktreespring.Domain.User;
import com.booktree.booktreespring.Jwt.JwtTokenProvider;
import com.booktree.booktreespring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;

@Controller
public class LoginController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }


    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody SignInDto signInDto) {
        return userRepository.save(User.builder()
                        .userName(signInDto.getUsername())
                        .userPwd(passwordEncoder.encode(signInDto.getPassword()))
                        .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                        .build()).getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody SignInDto signInDto) {
        User member = userRepository.findByUserName(signInDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
