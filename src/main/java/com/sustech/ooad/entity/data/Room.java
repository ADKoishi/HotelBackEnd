package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Room {
    Integer id;
    Integer floorId;
    Integer hotelId;
    Integer name;
    Integer category;
}
