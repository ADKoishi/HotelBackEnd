package com.sustech.ooad.entity.geoInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class State {
    Integer id;
    String name;
    Integer country_id;
    String country_code;
    String fips_code;
    String iso2;
    String type;
    Double latitude;
    Double longitude;
}
