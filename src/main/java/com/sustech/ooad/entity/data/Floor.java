package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Floor {
    Integer hotelId;
    Integer number;
    Boolean floorPlan;
    Boolean deleted;
}
