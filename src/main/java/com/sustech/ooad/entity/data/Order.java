package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;

@Data
public class Order {
    String order_number;
    Integer userId;
    Double pice;
    Integer hotelId;
    Integer roomId;
    Date startDate;
    Date endDate;
    Integer people;
    Integer children;
    Integer points;
    Boolean deleted;
}
