package com.sustech.ooad.service.impl;

import com.sustech.ooad.entity.geoInfo.*;
import com.sustech.ooad.mapper.geoInfoMappers.CityMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CountryMapper;
import com.sustech.ooad.mapper.geoInfoMappers.StateMapper;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MapSelectionServiceImpl implements MapSelectionService {

    @Autowired
    CountryMapper countryMapper;
    @Override
    public List<SimplifiedCountry> getAllCountries() {
        List<Country> countryList = countryMapper.getAllCountries();
        List<SimplifiedCountry> simplifiedCountryList = new ArrayList<>();
        for (Country country: countryList){
            simplifiedCountryList.add(new SimplifiedCountry(
                    country.getName(),
                    country.getIso2()
            ));
        }
        return simplifiedCountryList;
    }

    @Autowired
    StateMapper stateMapper;
    @Override
    public List<SimplifiedState> getStatesByCountryCode(String countryCode) {
        List<State> stateList = stateMapper.getStatesByCountryCode(countryCode);
        List<SimplifiedState> simplifiedStateList = new ArrayList<>();
        for (State state: stateList){
            simplifiedStateList.add(new SimplifiedState(
                    state.getName(),
                    state.getIso2()
            ));
        }

        return simplifiedStateList;
    }

    @Autowired
    CityMapper cityMapper;
    @Override
    public List<SimplifiedCity> getCitiesByStateCode(String countryCode ,String stateCode) {
        List<City> cityList = cityMapper.getCitiesByStateCode(countryCode, stateCode);
        List<SimplifiedCity> simplifiedCityList = new ArrayList<>();
        Base64.Encoder cityNameEncoder = Base64.getEncoder();
        for(City city: cityList){
            String aggregatedName = countryCode + "-" + stateCode + "-" + city.getName();
            String B64CityName = cityNameEncoder.encodeToString(aggregatedName.getBytes());
            simplifiedCityList.add(new SimplifiedCity(city.getName(), B64CityName));
        }

        return simplifiedCityList;
    }
}
