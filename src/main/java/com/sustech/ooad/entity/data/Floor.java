package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Floor {
    Integer hotel_id;
    Integer number;
    Boolean floor_plan;
    Boolean deleted;
}
