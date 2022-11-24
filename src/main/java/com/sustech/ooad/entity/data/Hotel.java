package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Hotel {
    Integer id;
    Double longitude;
    Double latitude;
    String name;
    String contact_code;
    String contact;
    String address;
    Integer scale;
    Integer priority;
    Boolean picture;
    Boolean deleted;
}
