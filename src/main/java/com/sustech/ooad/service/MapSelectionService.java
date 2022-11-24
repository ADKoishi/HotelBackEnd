package com.sustech.ooad.service;

import com.sustech.ooad.entity.geoInfo.*;

import java.util.List;
import java.util.Map;

public interface MapSelectionService {
    List<SimplifiedCountry> getAllCountries();

    List<SimplifiedState> getStatesByCountryCode(String countryCode);

    List<SimplifiedCity> getCitiesByStateCode(String CountryCode, String stateCode);
}
