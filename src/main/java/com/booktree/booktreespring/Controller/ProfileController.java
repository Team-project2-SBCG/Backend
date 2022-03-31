package com.booktree.booktreespring.Controller;

import com.booktree.booktreespring.Domain.Dto.ProfileDto;
import com.booktree.booktreespring.Domain.User;
import com.booktree.booktreespring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 프로필 반환 api
     */
    @PostMapping("/userInfo")
    @ResponseBody
    public ProfileDto getUserProfile(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        Optional<User> result = userRepository.findByUserName(username);
        User user = result.get();
        ProfileDto profileDto = ProfileDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .build();
        return profileDto;
    }

    /**
     * 사용자 아이디 변경 api
     */
    @PutMapping("/userInfo")
    @ResponseBody
    public ProfileDto updateUserProfile(@RequestBody String newName){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        Optional<User> result = userRepository.findByUserName(username);
        User changeUser = result.get();
        changeUser.setUserName(newName);
        userRepository.save(changeUser);

        ProfileDto profileDto = ProfileDto.builder()
                .username(changeUser.getUsername())
                .id(changeUser.getId())
                .build();

        return profileDto;
    }
}


