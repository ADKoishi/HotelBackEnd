package com.sustech.ooad.controller;

import com.sustech.ooad.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/user/info")
    public Map<String, Object> getUserInfo(
            @RequestBody Map<String, String> requestInfo
    ){
        Map<String, Object> userInfoResponse = new HashMap<>();
        userInfoService.getUserInfoByJWT(requestInfo, userInfoResponse);
        return userInfoResponse;
    }

    @PostMapping("/user/statistics")
    public Map<String, Object> getUserStatistics(
            @RequestBody Map<String, String> requestInfo
    ){
        Map<String, Object> userStatisticsResponse = new HashMap<>();
        userInfoService.getUserStatisticsByJWT(requestInfo, userStatisticsResponse);
        return userStatisticsResponse;
    }

    @PostMapping("/user/data")
    public List<Object> getUserData(
            @RequestBody Map<String, String> requestInfo
    ){
        List<Object> userDataResponse = new ArrayList<>();
        userInfoService.getUserDataByJWT(requestInfo, userDataResponse);
        return userDataResponse;
    }

    @PostMapping("/user/cancel")
    public List<Object> userCancel(
            @RequestBody Map<String, String> requestInfo
    ){
        List<Object> cancelResponse = new ArrayList<>();
        userInfoService.cancelOrder(requestInfo, cancelResponse);
        return cancelResponse;
    }
}
