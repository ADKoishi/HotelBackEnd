package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Notice {
    Integer id;
    String tile;
    String content;
    Boolean picture;
    Boolean deleted;
}
