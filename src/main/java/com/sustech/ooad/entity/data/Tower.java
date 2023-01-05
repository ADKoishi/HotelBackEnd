package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Tower {
    Integer id;
    Integer hotelId;
    String name;
    Integer lowestFloor;
    Integer highestFloor;
}
