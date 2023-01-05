package com.sustech.ooad.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Order {
    Integer id;
    Integer userId;
    Double price;
    Integer hotelId;
    Integer roomId;
    Date startDate;
    Date endDate;
    Integer people;
    Integer children;
    Integer points;
    Boolean deleted;
}
