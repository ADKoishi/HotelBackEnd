package com.sustech.ooad.service;

import com.sustech.ooad.entity.data.User;
import com.sustech.ooad.entity.response.ForgetPasswordResponse;
import com.sustech.ooad.entity.response.SignUpResponse;

import java.util.HashMap;
import java.util.Map;

public interface CustomerAccountService {
    void signUp(Map<String, String> signUpInfo, SignUpResponse signUpResponse);

    void resetPassword(Map<String, String> resetPasswordInfo, ForgetPasswordResponse forgetPasswordResponse);

    void signIn(Map<String, String> signInInfo, Map<String, String> signInResponse);

    void checkJWT(String JWTToken, Map<String, String> JWTCheckResponse);

    void getUsernameByJWT(Map<String, String> requestInfo, Map<String, String> getUsernameResponse);
}
