package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;


@Data
public class Customer {
    Integer id;
    String namePrefix;
    String nameSuffix;
    String phoneNumber;
    String phoneHead;
    String mail;
    Date birthday;
    String gender;
    Integer points;
    Integer accumulated;
    String country;
    Integer lastVisited;
    Boolean deleted;
}
