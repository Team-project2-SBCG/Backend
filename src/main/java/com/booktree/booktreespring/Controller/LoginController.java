package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Dto.SignInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {


    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 회원가입 api
     */
    @GetMapping("/signIn")
    public String signInController(@RequestBody SignInDto signInDto){
        return jwtTokenProvider.makeJwtToken(signInDto.getId());
    }

    /**
     * 로그인 api
     */
}
