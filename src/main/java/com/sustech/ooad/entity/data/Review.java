package com.sustech.ooad.entity.data;

import lombok.Data;

import java.sql.Date;

@Data
public class Review {
    Integer id;
    Integer userId;
    Integer orderId;
    String language;
    String title;
    Integer type;
    Integer stars;
    Date postDate;
    Date editDate;
    String description;
    Boolean picture;
    Boolean videos;
    Boolean visible;
    Integer deleted;
}
