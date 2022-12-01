package com.sustech.ooad.service.impl;

import com.sustech.ooad.entity.data.Hotel;
import com.sustech.ooad.entity.geoInfo.*;
import com.sustech.ooad.mapper.dataMappers.HotelMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CityMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CountryMapper;
import com.sustech.ooad.mapper.geoInfoMappers.StateMapper;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
            String B64CityName = cityNameEncoder.encodeToString(String.valueOf(city.getId()).getBytes());
            simplifiedCityList.add(new SimplifiedCity(city.getName(), B64CityName));
        }

        return simplifiedCityList;
    }

    @Autowired
    HotelMapper hotelMapper;
    Integer RETURN_CNT = 10;
    @Override
    public void getSortedHotels(String cityCode, Map<String, String>requestInfo, Map<String, Object> hotelMap) {
        List<Hotel> hotelList = null;
        if (cityCode == null){
            String longitude = requestInfo.get("longitude");
            String latitude = requestInfo.get("latitude");
            Double floatLongitude = 0., floatLatitude = 0.;
            String userIP = requestInfo.get("user_ip");
            if((longitude == null || latitude == null)){
                if(userIP != null){
                    floatLongitude = 1.;
                    floatLatitude = 1.;
                }
                else {
                    hotelMap.put("code", "-1");
                    hotelMap.put("msg", "Key name 'user_ip' not found.");
                    return;
                }
            }else{
                floatLongitude = Double.parseDouble(longitude);
                floatLatitude = Double.parseDouble(latitude);
            }
            hotelList = hotelMapper.getHotelByCoordinate(floatLongitude, floatLatitude, RETURN_CNT);
        }
        else {
            String sortStrategy = requestInfo.get("sort");
            if (sortStrategy == null){
                hotelMap.put("code", "-1");
                hotelMap.put("msg", "Key name 'sort' not found.");
                return;
            }
            Base64.Decoder base64Decoder = Base64.getDecoder();
            Integer cityID = Integer.parseInt(
                    new String(base64Decoder.decode(cityCode.getBytes()), StandardCharsets.UTF_8)
            );
        }
        if (hotelList == null){
            hotelMap.put("code", "-1");
            hotelMap.put("msg", "Query failed, returned 'hotelList' is null.");
            return;
        }
        for (int i = 0 ; i < hotelList.size(); i ++)
            hotelMap.put(String.valueOf(i), hotelList.get(i));
    }
}
