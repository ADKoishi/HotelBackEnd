package com.sustech.ooad.service;

import com.sustech.ooad.entity.GeoInfo.Country;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MapSelectionService {
    List<Country> getAllCountries();
}
