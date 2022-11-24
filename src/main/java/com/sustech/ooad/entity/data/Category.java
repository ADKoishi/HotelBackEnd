package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Category {
    Integer id;
    Integer hotel_id;
    String name;
    Integer price;
    Integer points;
    String description;
    Boolean picture;
    Integer deleted;
}
