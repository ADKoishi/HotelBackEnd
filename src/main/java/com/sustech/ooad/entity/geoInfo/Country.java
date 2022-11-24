package com.sustech.ooad.entity.geoInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Country {
    Integer id;
    String name;
    String iso3;
    String numericCode;
    String iso2;
    String phoneCode;
    String currency;
    String currencySymbol;
    String region;
    Double latitude;
    Double longitude;
}
