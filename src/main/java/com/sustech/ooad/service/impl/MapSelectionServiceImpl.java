package com.sustech.ooad.service.impl;

import com.sustech.ooad.entity.GeoInfo.Country;
import com.sustech.ooad.service.MapSelectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapSelectionServiceImpl implements MapSelectionService {
    @Override
    public List<Country> getAllCountries() {
        List<Country> countryList = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales){
            Locale locale = new Locale("", countryCode);
            countryList.add(new Country(locale.getDisplayCountry(), locale.getCountry()));
        }
        return countryList;
    }
}
