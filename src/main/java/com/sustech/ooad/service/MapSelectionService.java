package com.sustech.ooad.service;

import com.alibaba.fastjson.JSONObject;
import com.sustech.ooad.entity.GeoInfo.City;
import com.sustech.ooad.entity.GeoInfo.Country;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MapSelectionService {
    List<Country> getAllCountries();

    List<City> getCitiesByCountryCode(String countryCode);
}
