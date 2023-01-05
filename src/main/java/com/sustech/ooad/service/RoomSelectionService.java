package com.sustech.ooad.service;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface RoomSelectionService {
    void getHotelInfo(
            String hotelCode,
            Map<String, Object> HotelInfoResponse
    );
    String getRoomCover(String categoryCode);
    String getRoomGalleryPicture(String categoryCode, String idx);
}
