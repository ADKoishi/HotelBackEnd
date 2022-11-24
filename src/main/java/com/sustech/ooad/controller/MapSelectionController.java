package com.sustech.ooad.controller;

import com.sustech.ooad.entity.GeoInfo.City;
import com.sustech.ooad.entity.GeoInfo.Country;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class MapSelectionController {

    @Autowired
    MapSelectionService mapSelectionService;
    @GetMapping("/resources/map/countries")
    public List<Country> getAllCountries(){
        List<Country> countryList = mapSelectionService.getAllCountries();
        return countryList;
    }

    @GetMapping("/resources/map/regions/{country_code}")
    public List<City> getProvinceByCountryCode(
            @PathVariable(name = "country_code") String country_code
    ){
        List<City> cityList = mapSelectionService.getCitiesByCountryCode(country_code);
        return cityList;
    }
}
