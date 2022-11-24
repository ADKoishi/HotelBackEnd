package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class User {
    Integer id;
    Integer role_id;
    Integer label_id;
    String firstname;
    String lastname;
    Timestamp enrolled_date;
    String language;
    String password;
    Boolean avatar;
    Integer deleted;
}
