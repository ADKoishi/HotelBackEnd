package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;


@Data
public class Customer {
    Integer id;
    String name_prefix;
    String name_suffix;
    String phone_number;
    String phone_head;
    String mail;
    Date birthday;
    String gender;
    Integer points;
    Integer accumulated;
    String country;
    Integer last_visited;
    Boolean deleted;
}
