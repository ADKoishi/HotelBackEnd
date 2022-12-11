package com.sustech.ooad.controller;

import com.sustech.ooad.entity.geoInfo.*;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MapSelectionController {

    @Autowired
    MapSelectionService mapSelectionService;
    @GetMapping("/resources/map/countries")
    public List<SimplifiedCountry>  getAllCountries(){
        List<SimplifiedCountry> countryList = mapSelectionService.getAllCountries();
        return countryList;
    }

    @GetMapping("/resources/map/regions/{countryCode}")
    public List<SimplifiedState> getStatesByCountryCode(
            @PathVariable(name = "countryCode") String countryCode
    ){
        List<SimplifiedState> stateList = mapSelectionService.getStatesByCountryCode(countryCode);
        return stateList;
    }

    @GetMapping("/resources/map/cities/{countryCode}/{stateCode}")
    public List<SimplifiedCity> getCitiesByStateCode(
            @PathVariable(name = "countryCode") String countryCode,
            @PathVariable(name = "stateCode") String stateCode
    ){
        List<SimplifiedCity> cityList = mapSelectionService.getCitiesByStateCode(countryCode, stateCode);
        return cityList;
    }

    @GetMapping("/resources/map/hotels/")
    public Map<String, Object> getSortedHotels(
            @RequestBody Map<String, String> requestInfo
    ){
        Map<String, Object> hotelMap = new HashMap<>();
        mapSelectionService.getSortedHotels(requestInfo, hotelMap);
        return hotelMap;
    }
}
