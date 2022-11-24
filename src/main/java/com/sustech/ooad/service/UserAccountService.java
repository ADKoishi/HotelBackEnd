package com.sustech.ooad.service;

import com.sustech.ooad.entity.geoInfo.Country;

import java.sql.Date;

public interface UserAccountService {
    Boolean SignUp(
            String firstName,
            String lastName,
            String mail,
            String country,
            Date birthday
    );
}
