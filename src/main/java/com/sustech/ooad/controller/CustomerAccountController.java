package com.sustech.ooad.controller;

import com.sustech.ooad.entity.response.SimpleResult;
import com.sustech.ooad.service.CustomerAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Slf4j
@RestController
public class CustomerAccountController {

    @Autowired
    CustomerAccountService customerAccountService;

    @PostMapping("/signup")
    public SimpleResult customerSignUp(@RequestBody Map<String, String> signUpInfo){

        SimpleResult simpleResult = new SimpleResult("");
        Boolean signUpSuccess = customerAccountService.SignUp(signUpInfo);
        simpleResult.setReturnCode(signUpSuccess ? "0" : "1");
        return simpleResult;
    }
}
