package com.sustech.ooad.controller;

import com.sustech.ooad.property.StaticProp;
import com.sustech.ooad.service.RoomSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoomSelectionController {

    @Autowired
    RoomSelectionService roomSelectionService;

    @GetMapping("/resources/select/{hotelCode}")
    public Map<String, Object> getHotelInfo(
            @PathVariable(name = "hotelCode") String hotelCode
    ){
        Map<String, Object> HotelInfoResponse = new HashMap<>();
        roomSelectionService.getHotelInfo(hotelCode, HotelInfoResponse);
        return HotelInfoResponse;
    }
    @PostMapping("/resources/select/rooms")
    public Map<String, Object> getFloorRooms(
            @RequestBody Map<String, String> requestInfo
    ){
        Map<String, Object> floorRoomsResponse = new HashMap<>();
        roomSelectionService.getFloorRooms(requestInfo, floorRoomsResponse);
        return floorRoomsResponse;
    }
    @GetMapping("/static/cover/rooms/{categoryCode}")
    public String getHotelCover(
            @PathVariable(name = "categoryCode") String hotelCode
    ){
        return roomSelectionService.getRoomCover(hotelCode);
    }
    @GetMapping("/static/gallery/rooms/{categoryCode}/{idx}")
    public String getRoomGalleryPicture(
            @PathVariable(name = "categoryCode") String hotelCode,
            @PathVariable(name = "idx") String idx
    ){
        return roomSelectionService.getRoomGalleryPicture(hotelCode, idx);
    }
}
