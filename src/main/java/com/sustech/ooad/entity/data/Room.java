package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Room {
    Integer id;
    Integer floor_id;
    Integer hotel_id;
    Integer name;
    Boolean accessibility;
    Integer max_people;
    Integer max_children;
    Integer category;
    Integer deleted;
}
