package com.sustech.ooad.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sustech.ooad.entity.data.Hotel;
import com.sustech.ooad.entity.geoInfo.*;
import com.sustech.ooad.mapper.dataMappers.HotelMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CityMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CountryMapper;
import com.sustech.ooad.mapper.geoInfoMappers.StateMapper;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    @Autowired
    RestTemplate restTemplate;
    Integer RETURN_CNT = 10;
    String APP_KEY = "ZJFBZ-IMOLU-R4MVV-23C4K-TPK6J-24F4U";
    @Override
    public void getSortedHotels(
            Map<String, String>requestInfo,
            Map<String, Object> hotelResponse
    ){
        String cityCode = requestInfo.get("city_code");
        List<Hotel> hotelList = null;
        if (cityCode == null){
            String longitude = requestInfo.get("longitude");
            String latitude = requestInfo.get("latitude");
            double floatLongitude, floatLatitude;
            String userIP = requestInfo.get("user_ip");
            if((longitude == null || latitude == null)){
                if(userIP != null){
                    JSONObject forObject = restTemplate.getForObject(
                            "https://apis.map.qq.com/ws/location/v1/ip?ip="+userIP+"&key="+APP_KEY,
                            JSONObject.class
                    );
                    if (forObject == null){
                        hotelResponse.put("code", "-1");
                        hotelResponse.put("msg", "Fetch from 'apis.map.qq.com' failed");
                        return;
                    }
                    JSONObject location = forObject.getJSONObject("result").getJSONObject("location");
                    floatLongitude = Double.parseDouble(location.get("lng").toString());
                    floatLatitude = Double.parseDouble(location.get("lat").toString());
                }
                else {
                    hotelResponse.put("code", "-1");
                    hotelResponse.put("msg", "Key name 'user_ip' not found.");
                    return;
                }
            }else{
                floatLongitude = Double.parseDouble(longitude);
                floatLatitude = Double.parseDouble(latitude);
            }
            hotelList = hotelMapper.getHotelsByCoordinate(floatLongitude, floatLatitude, RETURN_CNT);
        }
        else {
            String sortStrategy = requestInfo.get("sort");
            if (sortStrategy == null){
                hotelResponse.put("code", "-1");
                hotelResponse.put("msg", "Key name 'sort' not found.");
                return;
            }
            int SortId = 0;
            try{
                SortId = Integer.parseInt(sortStrategy);
            }catch (Exception e){
                hotelResponse.put("code", "-1");
                hotelResponse.put("msg", "Failed to parse 'sort': " + sortStrategy + " to integer.");
                return;
            }
            Base64.Decoder base64Decoder = Base64.getDecoder();
            Integer cityId = Integer.parseInt(
                    new String(base64Decoder.decode(cityCode.getBytes()), StandardCharsets.UTF_8)
            );
            switch (SortId){
                case 0:
                    hotelList = hotelMapper.getHotelsByCityIdS0(cityId);
                    break;
                default:
                    hotelList = hotelMapper.getHotelsByCityIdS0(cityId);
                    break;
            }

        }
        if (hotelList == null){
            hotelResponse.put("code", "-1");
            hotelResponse.put("msg", "Query failed, returned 'hotelList' is null.");
            return;
        }
        Map<String, String> responseObject;
        for (int i = 0 ; i < hotelList.size(); i ++) {
            Hotel hotel = hotelList.get(i);
            responseObject = new HashMap<>();
            responseObject.put("name", hotel.getName());
            responseObject.put("id", String.valueOf(hotel.getId()));
            //startingPrice
            //avaliableRates
            responseObject.put("accessible", String.valueOf(hotelMapper.getAccessibleRoomCount(hotel.getId()) > 0));
            responseObject.put("points", String.valueOf(hotel.getPointsAvail()));
            //amenities
            //gallerySize
            responseObject.put("longitude", String.valueOf(hotel.getLongitude()));
            responseObject.put("latitude", String.valueOf(hotel.getLatitude()));
            responseObject.put("link", hotel.getLink());
            //favorited
            responseObject.put("description", hotel.getDescription());
            responseObject.put("location", hotel.getAddress());
            Integer countryId = hotelMapper.getHotelCountryIdByHotelId(hotel.getId());
            String currency = countryMapper.getCurrencyById(countryId);
            String currencySymbol = countryMapper.getCurrencySymbolById(countryId);
            responseObject.put("prefix", currencySymbol);
            responseObject.put("currency", currency);
            hotelResponse.put(String.valueOf(i), responseObject);
        }
    }
}
