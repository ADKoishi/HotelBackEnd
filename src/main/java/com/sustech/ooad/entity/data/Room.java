package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Room {
    Integer id;
    Integer floor;
    Integer hotelId;
    Integer towerId;
    String name;
    Integer category;
}
