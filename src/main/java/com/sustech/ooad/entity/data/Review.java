package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;

@Data
public class Review {
    Integer id;
    Integer user_id;
    String order_number;
    String language;
    String title;
    Integer type;
    Integer stars;
    Date post_date;
    Date edit_date;
    String description;
    Boolean picture;
    Boolean videos;
    Boolean visible;
    Integer deleted;
}
