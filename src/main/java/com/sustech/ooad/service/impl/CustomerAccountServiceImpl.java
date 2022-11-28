package com.sustech.ooad.service.impl;

import com.sustech.ooad.entity.data.Customer;
import com.sustech.ooad.entity.data.User;
import com.sustech.ooad.entity.response.SimpleResult;
import com.sustech.ooad.mapper.dataMappers.CustomerMapper;
import com.sustech.ooad.mapper.dataMappers.UserMapper;
import com.sustech.ooad.service.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Boolean SignUp(
            Map<String, String> signUpInfo
    ) {

        String firstname = signUpInfo.get("firstname");
        String lastname = signUpInfo.get("lastname");
        String mail = signUpInfo.get("mail");
        String country = signUpInfo.get("country");
        String password = signUpInfo.get("password");
        Date birthday = null;
        Customer customer = customerMapper.getCustomerByMail(mail);
        if (customer != null)
            return false;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date UDate = simpleDateFormat.parse(signUpInfo.get("birthday"));
            birthday = new java.sql.Date(UDate.getTime());
        } catch (ParseException e) {
            return false;
        }

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(password);
        userMapper.signUpInsert(user);
        Integer userID = user.getId();

        customer = new Customer();
        customer.setUser_id(userID);
        customer.setMail(mail);
        customer.setCountry(country);
        customer.setBirthday(birthday);
        customerMapper.signUpInsert(customer);
        return true;
    }
}
