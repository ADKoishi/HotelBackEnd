package com.sustech.ooad.service;

import java.util.List;
import java.util.Map;

public interface UserInfoService {
    void getUserInfoByJWT(Map<String, String> requestInfo, Map<String, Object> userInfoResponse);

    void getUserStatisticsByJWT(Map<String, String> requestInfo, Map<String, Object> userStatisticsResponse);

    void getUserDataByJWT(Map<String, String> requestInfo, List<Object> userDataResponse);

    void cancelOrder(Map<String, String> requestInfo, List<Object> cancelResponse);
}
