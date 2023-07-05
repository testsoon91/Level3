package com.sparta.blog.controller;

import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.dto.StatusCodeDto;
import com.sparta.blog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login-page")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage(){
        return "signup";
    }

    @PostMapping("/user/signup")
    public StatusCodeDto signup(@RequestBody @Valid SignupRequestDto requestDto){
        userService.signup(requestDto);
        return new StatusCodeDto("회원가입 성공", HttpStatus.OK.value());
    }

    @PostMapping("/user/login")
    public StatusCodeDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res){
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            System.out.println("로그인 실패");
        }
        return new StatusCodeDto("로그인 성공", HttpStatus.OK.value());
    }
}
