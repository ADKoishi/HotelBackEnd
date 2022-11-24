package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;

@Data
public class Order {
    String order_number;
    Integer user_id;
    Double pice;
    Integer hotel_id;
    Integer room_id;
    Date start_date;
    Date end_date;
    Integer people;
    Integer children;
    Integer points;
    Boolean deleted;
}
