package com.sustech.ooad.controller;

import com.sustech.ooad.entity.response.ForgetPasswordResponse;
import com.sustech.ooad.entity.response.SignUpResponse;
import com.sustech.ooad.service.CustomerAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class CustomerAccountController {

    @Autowired
    CustomerAccountService customerAccountService;

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody Map<String, String> signUpInfo){
        SignUpResponse signUpResponse = new SignUpResponse();
        customerAccountService.signUp(signUpInfo, signUpResponse);
        return signUpResponse;
    }

    @PostMapping("/forgetPsw")
    public ForgetPasswordResponse resetPasswd(@RequestBody Map<String, String> resetPasswordInfo){
        ForgetPasswordResponse forgetPasswordResponse = new ForgetPasswordResponse();
        customerAccountService.resetPassword(resetPasswordInfo, forgetPasswordResponse);
        return forgetPasswordResponse;
    }
    @PostMapping("/signin")
    public Map<String, String> signIn(@RequestBody Map<String, String> signInInfo){
        Map<String, String> signInResponse = new HashMap<>();
        customerAccountService.signIn(signInInfo, signInResponse);
        return signInResponse;
    }

    @GetMapping("/isTokenOk")
    public Map<String, String> checkJWT(@RequestParam String JWT){
        Map<String, String> JWTCheckResponse = new HashMap<>();
        customerAccountService.checkJWT(JWT, JWTCheckResponse);
        return JWTCheckResponse;
    }

}
