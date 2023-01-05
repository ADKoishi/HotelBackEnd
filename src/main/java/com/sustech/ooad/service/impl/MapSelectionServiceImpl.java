package com.sustech.ooad.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sustech.ooad.Utils.JWTUtils;
import com.sustech.ooad.Utils.PathUtils;
import com.sustech.ooad.entity.data.Hotel;
import com.sustech.ooad.entity.data.User;
import com.sustech.ooad.entity.geoInfo.*;
import com.sustech.ooad.mapper.dataMappers.HotelMapper;
import com.sustech.ooad.mapper.dataMappers.UserMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CityMapper;
import com.sustech.ooad.mapper.geoInfoMappers.CountryMapper;
import com.sustech.ooad.mapper.geoInfoMappers.StateMapper;
import com.sustech.ooad.property.PricingProp;
import com.sustech.ooad.property.StaticProp;
import com.sustech.ooad.service.CustomerAccountService;
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
    @Autowired
    StateMapper stateMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserMapper userMapper;
    Integer RETURN_CNT = 10;
    String APP_KEY = "ZJFBZ-IMOLU-R4MVV-23C4K-TPK6J-24F4U";
    @Autowired
    StaticProp staticProp;
    @Autowired
    CustomerAccountService customerAccountService;
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

    @Override
    public void getSortedHotels(
            Map<String, String> requestInfo,
            Map<String, Object> hotelResponse
    ){
        String jwt = requestInfo.get("jwt");
        DecodedJWT decodedJWT;
        boolean userLogin = false;
        Integer userId = null;
        if(jwt != null)
            try {
                decodedJWT = JWTUtils.decode(jwt);
                userId = Integer.parseInt(
                        decodedJWT.getClaim("user_id").toString().replaceAll("\"","")
                );
                userLogin = true;
            } catch (Exception ignored){}

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
                        hotelResponse.put("code", -1);
                        hotelResponse.put("msg", "Fetch from 'apis.map.qq.com' failed");
                        return;
                    }
                    JSONObject location = forObject.getJSONObject("result").getJSONObject("location");
                    floatLongitude = Double.parseDouble(location.get("lng").toString());
                    floatLatitude = Double.parseDouble(location.get("lat").toString());
                }
                else {
                    hotelResponse.put("code", -1);
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
                hotelResponse.put("code", -1);
                hotelResponse.put("msg", "Key name 'sort' not found.");
                return;
            }
            int SortId = 0;
            try{
                SortId = Integer.parseInt(sortStrategy);
            }catch (Exception e){
                hotelResponse.put("code", -1);
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
            hotelResponse.put("code", -1);
            hotelResponse.put("msg", "Query failed, returned 'hotelList' is null.");
            return;
        }
        Map<String, Object> responseObject;
        List<Object> responseList = new ArrayList<>();
        for (int i = 0 ; i < hotelList.size(); i ++) {
            Hotel hotel = hotelList.get(i);
            responseObject = new HashMap<>();
            responseObject.put("name", hotel.getName());
            responseObject.put("id", String.valueOf(hotel.getId()));
            List<Double> prices = new ArrayList<>();
            Double standardStartPrice = hotelMapper.getCheapestAvail(hotel.getId(), "^1..$");
            prices.add(standardStartPrice == null ? -1. : standardStartPrice * PricingProp.STANDARD_RATE);
            Double studentStartPrice = hotelMapper.getCheapestAvail(hotel.getId(), "^.1.$");
            prices.add(studentStartPrice == null ? -1. : studentStartPrice * PricingProp.STUDENT_RATE);
            Double militaryStartPrice = hotelMapper.getCheapestAvail(hotel.getId(), "^.1.$");
            prices.add(militaryStartPrice == null ? -1. : militaryStartPrice * PricingProp.MILITARY_RATE);
            responseObject.put("prices", prices);
            String standardRateAvail = hotelMapper.getRateAvail(hotel.getId(), "^1..$") ? "1" : "0";
            String studentRateAvail = hotelMapper.getRateAvail(hotel.getId(), "^.1.$") ? "1" : "0";
            String militaryRateAvail = hotelMapper.getRateAvail(hotel.getId(), "^..1$") ? "1" : "0";
            responseObject.put("available_rates",
                    Integer.parseInt(standardRateAvail+studentRateAvail+militaryRateAvail, 2));
            responseObject.put("accessible", hotelMapper.hasAccessibleRoom(hotel.getId()));
            responseObject.put("points", hotel.getPointsAvail());
            responseObject.put("amenities", Integer.parseInt(hotel.getAmenities(), 2));
            String hotelGalleryPath = "/gallery/hotels/" + hotel.getId();
            responseObject.put("gallery_size", PathUtils.directoryCount(
                    staticProp.getStaticDirectory() + hotelGalleryPath)
            );
            responseObject.put("longitude", hotel.getLongitude());
            responseObject.put("latitude", hotel.getLatitude());
            responseObject.put("link", hotel.getLink());
            if(userLogin) {
                String userFavorites = userMapper.getFavoritesById(userId);
                if(userFavorites.length() > 0){
                    String[] userFavoritesArr = userFavorites.split(",");
                    for (String s : userFavoritesArr)
                        if(hotel.getId() == Integer.parseInt(s)){
                            responseObject.put("favorited", true);
                            break;
                        }
                } else responseObject.put("favorited", false);
            } else responseObject.put("favorited", false);
            responseObject.put("description", hotel.getDescription());
            responseObject.put("location", hotel.getAddress());
            Integer countryId = hotelMapper.getHotelCountryIdByHotelId(hotel.getId());
            String currency = countryMapper.getCurrencyById(countryId);
            String currencySymbol = countryMapper.getCurrencySymbolById(countryId);
            responseObject.put("prefix", currencySymbol);
            responseObject.put("currency", currency);
            responseList.add(responseObject);
        }
        hotelResponse.put("code", 0);
        hotelResponse.put("hotels", responseList);
    }

    @Override
    public String getHotelCover(String hotelCode) {
        String hotelCoverPath = "/cover/hotels/" + hotelCode;
        String altPath = "/cover/hotels/alt.png";
        String posix = PathUtils.getPicturePosix(staticProp.getStaticDirectory() + hotelCoverPath);
        if (posix == null)
            return "<img src=\"http://" + staticProp.getStaticUrl() + altPath + "\">";
        else return "<img src=\"http://" + staticProp.getStaticUrl() + hotelCoverPath + posix + "\">";
    }

    @Override
    public String getHotelGalleryPicture(String hotelCode, String idx) {
        String hotelGalleryPicturePath = "/gallery/hotels/" + hotelCode + "/" + idx;
        String altPath = "/gallery/hotels/alt.png";
        String posix = PathUtils.getPicturePosix(staticProp.getStaticDirectory() + hotelGalleryPicturePath);
        if (posix == null)
            return "<img src=\"http://" + staticProp.getStaticUrl() + altPath + "\">";
        else return "<img src=\"http://" + staticProp.getStaticUrl() + hotelGalleryPicturePath + posix + "\">";
    }

    @Override
    public void userFavorite(Map<String, String> requestInfo) {
        Map<String, String> JWTCheckResponse = new HashMap<>();
        String JWTToken = requestInfo.get("JWT");
        String hotelId = requestInfo.get("hotel");
        String favourite = requestInfo.get("favourite");
        if (JWTToken == null || hotelId == null || favourite == null)
            return;
        customerAccountService.checkJWT(JWTToken, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1"))
            return;
        if (hotelMapper.getHotelById(Integer.valueOf(hotelId)) == null)
            return;
        String userId = String.valueOf(JWTUtils.decode(JWTToken).getClaim("user_id"));
        userId = userId.replaceAll("\"", "");
        User user = userMapper.getUserById(Integer.valueOf(userId));
        String userFavorites = user.getFavorites();
        if (Boolean.parseBoolean(favourite)){
            String[] userFavoritesArr = userFavorites.split(",");
            boolean alreadyFavorite = false;
            for (String userFavorite : userFavoritesArr)
                alreadyFavorite = userFavorite.equals(hotelId) || alreadyFavorite;
            if (alreadyFavorite)
                return;
            if (userFavorites.length() != 0)
                userFavorites += ",";
            userFavorites += hotelId;
        }
        else {
            userFavorites = userFavorites.replaceAll("^" + hotelId + ",", "");
            userFavorites = userFavorites.replaceAll("," + hotelId + ",", ",");
            userFavorites = userFavorites.replaceAll("," + hotelId + "$", "");
        }
        userMapper.setFavoritesById(Integer.valueOf(userId), userFavorites);
    }
}
