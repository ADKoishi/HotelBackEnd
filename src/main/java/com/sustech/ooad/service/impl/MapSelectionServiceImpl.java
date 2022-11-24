package com.sustech.ooad.service.impl;

import com.sustech.ooad.entity.GeoInfo.Country;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MapSelectionServiceImpl implements MapSelectionService {
    @Override
    public List<Country> getAllCountries() {
        List<Country> countryList = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        Locale USLocale = new Locale("", "US");
        for (String countryCode : locales){
            Locale locale = new Locale("", countryCode);
            countryList.add(new Country(locale.getDisplayCountry(USLocale), locale.getCountry()));
        }
        return countryList;
    }
}
