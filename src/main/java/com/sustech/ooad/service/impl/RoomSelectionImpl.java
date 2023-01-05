package com.sustech.ooad.service.impl;

import com.sustech.ooad.Utils.PathUtils;
import com.sustech.ooad.entity.data.Hotel;
import com.sustech.ooad.entity.data.Tower;
import com.sustech.ooad.mapper.dataMappers.HotelMapper;
import com.sustech.ooad.mapper.dataMappers.TowerMapper;
import com.sustech.ooad.property.StaticProp;
import com.sustech.ooad.service.RoomSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomSelectionImpl implements RoomSelectionService {
    @Autowired
    StaticProp staticProp;
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    TowerMapper towerMapper;
    @Override
    public void getHotelInfo(
            String hotelCode,
            Map<String, Object> HotelInfoResponse) {
        Hotel hotel = hotelMapper.getHotelById(Integer.valueOf(hotelCode));
        List<Map<String, Object>> towerArr = new ArrayList<>();
        List<Map<String, Object>> roomArr = new ArrayList<>();
        List<Map<String, Object>> categoryArr = new ArrayList<>();
        HotelInfoResponse.put("towers", towerArr);
        HotelInfoResponse.put("rooms", roomArr);
        HotelInfoResponse.put("categories", categoryArr);
        if (hotel == null)
            return;
        List<Tower> towers = towerMapper.getTowersByHotelId(Integer.valueOf(hotelCode));
        Map<String, Object> singleTowerInfo;
        for (Tower tower : towers){
            singleTowerInfo = new HashMap<>();
            singleTowerInfo.put("id", tower.getId());
            singleTowerInfo.put("name", )
        }

    }

    @Override
    public String getRoomCover(String categoryCode) {
        String roomCoverPath = "/cover/rooms/" + categoryCode;
        String altPath = "/cover/rooms/alt.png";
        String posix = PathUtils.getPicturePosix(staticProp.getStaticDirectory() + roomCoverPath);
        if (posix == null)
            return "<img src=\"http://" + staticProp.getStaticUrl() + altPath + "\">";
        else return "<img src=\"http://" + staticProp.getStaticUrl() + roomCoverPath + posix + "\">";
    }

    @Override
    public String getRoomGalleryPicture(String categoryCode, String idx) {
        String roomGalleryPicturePath = "/gallery/rooms/" + categoryCode + "/" + idx;
        String altPath = "/gallery/rooms/alt.png";
        String posix = PathUtils.getPicturePosix(staticProp.getStaticDirectory() + roomGalleryPicturePath);
        if (posix == null)
            return "<img src=\"http://" + staticProp.getStaticUrl() + altPath + "\">";
        else return "<img src=\"http://" + staticProp.getStaticUrl() + roomGalleryPicturePath + posix + "\">";
    }
}
