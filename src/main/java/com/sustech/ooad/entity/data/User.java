package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class User {
    Integer id;
    Integer role;
    String labels;
    String firstname;
    String lastname;
    Timestamp enrolledDate;
    String language;
    String password;
    Boolean avatar;
    Integer deleted;
}
