package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Tower {
    Integer id;
    Integer hotel_id;
    String tower;
    Integer lowestFloor;
    Integer highestFloor;
}
