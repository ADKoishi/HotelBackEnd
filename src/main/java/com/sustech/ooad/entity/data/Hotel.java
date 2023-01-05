package com.sustech.ooad.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Hotel {
    Integer id;
    Integer cityId;
    Integer defaultTower;
    Double longitude;
    Double latitude;
    String name;
    String link;
    String contactCode;
    String contact;
    String address;
    String amenities;
    String description;
    Integer scale;
    Integer priority;
    Boolean picture;
    Boolean pointsAvail;
    Boolean deleted;
}
