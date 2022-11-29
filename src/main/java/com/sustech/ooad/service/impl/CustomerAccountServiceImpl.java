package com.sustech.ooad.service.impl;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sustech.ooad.Utils.JWTUtils;
import com.sustech.ooad.entity.data.Customer;
import com.sustech.ooad.entity.data.User;
import com.sustech.ooad.entity.response.ForgetPasswordResponse;
import com.sustech.ooad.entity.response.SignUpResponse;
import com.sustech.ooad.mapper.dataMappers.CustomerMapper;
import com.sustech.ooad.mapper.dataMappers.UserMapper;
import com.sustech.ooad.service.CustomerAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void signUp(Map<String, String> signUpInfo, SignUpResponse signUpResponse) {
        String firstname = signUpInfo.get("firstname");
        String lastname = signUpInfo.get("lastname");
        String mail = signUpInfo.get("mail");
        String country = signUpInfo.get("country");
        String password = signUpInfo.get("password");
        String birthdayTxt = signUpInfo.get("birthday");
        if (
            firstname == null
            || lastname == null
            || mail == null
            || country == null
            || password == null
            || birthdayTxt == null
        ){
            signUpResponse.setCode("-1");
            return;
        }

        Date birthday = null;

        Customer customer = customerMapper.getCustomerByMail(mail);
        if (customer != null) {
            signUpResponse.setCode("1");
            return;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date UDate = simpleDateFormat.parse(birthdayTxt);
            birthday = new java.sql.Date(UDate.getTime());
        } catch (Exception e) {
            signUpResponse.setCode("1");
            return;
        }
        String sha256Password = sha256Hex(password);
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(sha256Password);
        userMapper.signUpInsert(user);
        Integer userID = user.getId();

        customer = new Customer();
        customer.setId(userID);
        customer.setMail(mail);
        customer.setCountry(country);
        customer.setBirthday(birthday);
        customerMapper.signUpInsert(customer);
        signUpResponse.setCode("0");
    }

    @Override
    public void resetPassword(Map<String, String> resetPasswdInfo, ForgetPasswordResponse forgetPasswordResponse) {
        String mail = resetPasswdInfo.get("mail");
        String password = resetPasswdInfo.get("newpassword");
        if (mail == null || password == null){
            forgetPasswordResponse.setCode("-1");
            forgetPasswordResponse.setMessage("Data format invalid!");
            return;
        }
        Customer customer = customerMapper.getCustomerByMail(mail);
        if (customer == null){
            forgetPasswordResponse.setCode("400");
            forgetPasswordResponse.setMessage("The account does not exist!");
            return;
        }
        userMapper.setPasswdByID(customer.getId(), sha256Hex(password));
        forgetPasswordResponse.setCode("200");
        forgetPasswordResponse.setMessage("The new password is set successfully!");
    }

    @Override
    public void signIn(Map<String, String> signInInfo, Map<String, String> signInResponse) {
        String mail = signInInfo.get("mail");
        String passwordTxt = signInInfo.get("password");
        if (mail == null || passwordTxt == null){
            signInResponse.put("code", "-1");
            signInResponse.put("msg", "Data format invalid!");
            return;
        }
        String password = sha256Hex(passwordTxt);
        User user = userMapper.getUserByMail(mail);
        if (user == null || !password.equals(user.getPassword())){
            signInResponse.put("code", "0");
            signInResponse.put("msg", "fail");
            return;
        }
        Map<String, String> JWTPayload = new HashMap<>();
        JWTPayload.put("id", String.valueOf(user.getId()));
        String JWTToken = JWTUtils.getToken(JWTPayload);
        signInResponse.put("code", "1");
        signInResponse.put("JWT", JWTToken);
        return;
    }

    @Override
    public void checkJWT(String JWTToken, Map<String, String> JWTCheckResponse) {
        try {
            DecodedJWT decodedJWT = JWTUtils.decode(JWTToken);
            JWTCheckResponse.put("code", "1");
            JWTCheckResponse.put("msg", "Token valid!");
        }catch (SignatureVerificationException e){
            JWTCheckResponse.put("code", "0");
            JWTCheckResponse.put("msg", "Token signature invalid!");
        }catch (TokenExpiredException e){
            JWTCheckResponse.put("code", "0");
            JWTCheckResponse.put("msg", "Token expired!");
        }catch (AlgorithmMismatchException e){
            JWTCheckResponse.put("code", "0");
            JWTCheckResponse.put("msg", "Algorithm invalid!");
        }catch (Exception e){
            JWTCheckResponse.put("code", "0");
            JWTCheckResponse.put("msg", "Token invalid!");
        }
    }
}
