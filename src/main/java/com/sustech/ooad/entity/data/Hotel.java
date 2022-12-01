package com.sustech.ooad.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Hotel {
    Integer id;
    Integer cityId;
    Double longitude;
    Double latitude;
    String name;
    String contactCode;
    String contact;
    String address;
    Integer scale;
    Integer priority;
    Boolean picture;
    Boolean deleted;
}
