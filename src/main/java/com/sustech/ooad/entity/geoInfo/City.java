package com.sustech.ooad.entity.geoInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {
    Integer id;
    String name;
    Integer state_id;
    String state_code;
    Integer country_id;
    String country_code;
    Double latitude;
    Double longitude;
}
