package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Dto.JwtDto;
import com.booktree.booktreespring.Domain.Dto.SignInDto;
import com.booktree.booktreespring.Domain.User;
import com.booktree.booktreespring.Jwt.JwtTokenProvider;
import com.booktree.booktreespring.Repository.UserRepository;
import com.booktree.booktreespring.Util.BasicResponse;
import com.booktree.booktreespring.Util.CommonResponse;
import com.booktree.booktreespring.Util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

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

    /**
     * 회원가입 api
     */
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<? extends BasicResponse> join(@RequestBody SignInDto signInDto) {
        System.out.println("회원가입 요청입니다.");
        Optional<User> result = userRepository.findByUserName(signInDto.getUsername());
        if(result.isPresent()){
            return ResponseEntity.ok().body(new ErrorResponse("이미 존재하는 아이디입니다."));
        }else {
            return ResponseEntity.ok().body(new CommonResponse<>(userRepository.save(User.builder()
                    .userName(signInDto.getUsername())
                    .userPwd(passwordEncoder.encode(signInDto.getPassword()))
                    .build()).getId()));
        }
    }

    /**
     * 로그인 api
     */
    @PostMapping("/login1")
    @ResponseBody
    public ResponseEntity<? extends BasicResponse> login1(@RequestBody SignInDto signInDto) {
        System.out.println("1: 로그인 요청입니다.");
        User member = userRepository.findByUserName(signInDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        JwtDto jwtDto = new JwtDto(jwtTokenProvider.createToken(member.getUsername()));
        return ResponseEntity.ok().body(new CommonResponse<>(jwtDto));
    }

    /**
     * 에러 발생으로 인한 테스트 api 2
     */
    @PostMapping("/login2")
    @ResponseBody
    public JwtDto login2(@RequestBody SignInDto signInDto) {
        System.out.println("2: 로그인 요청입니다.");
        User member = userRepository.findByUserName(signInDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        JwtDto jwtDto = new JwtDto(jwtTokenProvider.createToken(member.getUsername()));
        return jwtDto;
    }

    /**
     * 에러 발생으로 인한 테스트 api 3
     */
    @PostMapping("/login3")
    @ResponseBody
    public String login3(@RequestBody SignInDto signInDto) {
        System.out.println("3: 로그인 요청입니다.");
        User member = userRepository.findByUserName(signInDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if (!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername());
    }


    /**
     * 회원탈퇴 api
     */
    @PostMapping("/withdraw")
    @ResponseBody
    public boolean withdraw(@RequestBody SignInDto signInDto){
        System.out.println("회원탈퇴 요청입니다. ");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = ((UserDetails) principal).getUsername();
        String password = ((UserDetails) principal).getPassword();
        System.out.println("탈퇴 name:" + username);

        Optional<User> findUser = userRepository.findByUserName(username);
        if(findUser.isPresent()) {
            if(findUser.get().getPassword().equals(password)) {
                userRepository.deleteById(findUser.get().getId());
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
}
