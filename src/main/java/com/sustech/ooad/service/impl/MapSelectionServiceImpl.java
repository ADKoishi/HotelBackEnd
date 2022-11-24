package com.sustech.ooad.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.ooad.Utils.WorldAddressUtils;
import com.sustech.ooad.entity.GeoInfo.City;
import com.sustech.ooad.entity.GeoInfo.Country;
import com.sustech.ooad.service.MapSelectionService;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public List<City> getCitiesByCountryCode(String countryCode) {
        List<City> cityList = new ArrayList<>();
        WorldAddressUtils worldAddressUtils = new WorldAddressUtils();
        JSONArray resultSet = worldAddressUtils.getProvinceFromCountry(countryCode)
                .getJSONArray("countrysProvince");
        if (resultSet == null){
            return cityList;
        }
        resultSet = resultSet
                .getJSONObject(0)
                .getJSONArray("provinceCity");

        Map<String, String> ENDict = worldAddressUtils.getCnEnglishMap();
        Base64.Encoder nameEncode = Base64.getEncoder();
        for (int i = 0; i < resultSet.size() ; i ++) {
            JSONObject PNCTuple = resultSet.getJSONObject(i);
            String CNProvinceName = PNCTuple.get("province").toString();
            String ENProvinceName = ENDict.get(CNProvinceName);
            String ProvinceCode = nameEncode.encodeToString(ENProvinceName.getBytes());
            cityList.add(new City(ENProvinceName, ProvinceCode));
        }
        return cityList;
    }
}
