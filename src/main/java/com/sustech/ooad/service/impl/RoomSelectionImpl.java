package com.sustech.ooad.service.impl;

import com.sustech.ooad.Utils.PathUtils;
import com.sustech.ooad.Utils.SVGUtils;
import com.sustech.ooad.entity.data.Category;
import com.sustech.ooad.entity.data.Hotel;
import com.sustech.ooad.entity.data.Room;
import com.sustech.ooad.entity.data.Tower;
import com.sustech.ooad.mapper.dataMappers.CategoryMapper;
import com.sustech.ooad.mapper.dataMappers.HotelMapper;
import com.sustech.ooad.mapper.dataMappers.RoomMapper;
import com.sustech.ooad.mapper.dataMappers.TowerMapper;
import com.sustech.ooad.property.PricingProp;
import com.sustech.ooad.property.StaticProp;
import com.sustech.ooad.service.RoomSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

@Service
public class RoomSelectionImpl implements RoomSelectionService {
    @Autowired
    StaticProp staticProp;
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    TowerMapper towerMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public void getHotelInfo(
            String hotelCode,
            Map<String, Object> HotelInfoResponse
    ) {
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
            singleTowerInfo.put("name", tower.getName());
            singleTowerInfo.put("lowest_floor", tower.getLowestFloor());
            singleTowerInfo.put("highest_floor", tower.getHighestFloor());
            towerArr.add(singleTowerInfo);
        }

        Tower hotelDefaultTower = towerMapper.getTowerById(hotel.getDefaultTower());
        List<Room> rooms = roomMapper.getRoomByTowerAndFloor(
                hotel.getDefaultTower(), hotelDefaultTower.getLowestFloor());
        String defaultFloorSVG = staticProp.getStaticDirectory()
                + "/tower/" + hotel.getDefaultTower() + "/floor/" + hotelDefaultTower.getLowestFloor() + ".svg";
        List<List<Double>> roomPercentageCoordinates;
        try {
            roomPercentageCoordinates = SVGUtils.parseSvgFile(defaultFloorSVG);
        } catch (IOException | SAXException e) {
            return;
        }
        assert roomPercentageCoordinates.size() == rooms.size();
        Map<String, Object> singleRoomInfo;
        for (int i = 0 ; i < rooms.size() ; i ++){
            singleRoomInfo = new HashMap<>();
            singleRoomInfo.put("id", rooms.get(i).getId());
            String roomName = rooms.get(i).getFloor() + String.format("%03d", i+1);
            singleRoomInfo.put("name", roomName);
            singleRoomInfo.put("category", rooms.get(i).getCategory());
            singleRoomInfo.put("coordinates", roomPercentageCoordinates.get(i));
            roomArr.add(singleRoomInfo);
        }

        List<Category> categories = categoryMapper.getCategoriesByHotelId(hotel.getId());
        Map<String, Object> singleCategoryInfo;
        for (Category category : categories){
            singleCategoryInfo = new HashMap<>();
            singleCategoryInfo.put("id", category.getId());
            singleCategoryInfo.put("name", category.getName());
            singleCategoryInfo.put("max_capacity", category.getMaxChildren() + category.getMaxPeople());
            singleCategoryInfo.put("max_children", category.getMaxChildren());
            String hotelGalleryPath = "/gallery/categories/" + category.getId();
            singleCategoryInfo.put("gallery_size", PathUtils.directoryCount(
                    staticProp.getStaticDirectory() + hotelGalleryPath));
            List<Double> prices = new ArrayList<>();
            prices.add(category.getAvailableRates().charAt(0) == '0' ? -1. : category.getPrice() * PricingProp.STANDARD_RATE);
            prices.add(category.getAvailableRates().charAt(1) == '0' ? -1. : category.getPrice() * PricingProp.STUDENT_RATE);
            prices.add(category.getAvailableRates().charAt(2) == '0' ? -1. : category.getPrice() * PricingProp.MILITARY_RATE);
            singleCategoryInfo.put("prices", prices);
            singleCategoryInfo.put("rates", Integer.parseInt(category.getAvailableRates(), 2));
            singleCategoryInfo.put("amenities", Integer.parseInt(category.getAmenities(), 2));
            singleCategoryInfo.put("prefix", category.getPrefix());
            singleCategoryInfo.put("currency", category.getCurrency());
            categoryArr.add(singleCategoryInfo);
        }

    }

    @Override
    public void getFloorRooms(
            String hotelCode,
            Map<String, String> requestInfo,
            Map<String, Object> FloorRoomsResponse
    ) {
        List<Map<String, Object>> roomArr = new ArrayList<>();
        FloorRoomsResponse.put("rooms", roomArr);
        String towerCode = requestInfo.get("tower");
        String floorNumber = requestInfo.get("floor");

        List<Room> rooms = roomMapper.getRoomByTowerAndFloor(
                Integer.parseInt(towerCode), Integer.parseInt(floorNumber));
        String defaultFloorSVG = staticProp.getStaticDirectory()
                + "/tower/" + towerCode + "/floor/" + floorNumber + ".svg";
        List<List<Double>> roomPercentageCoordinates;
        try {
            roomPercentageCoordinates = SVGUtils.parseSvgFile(defaultFloorSVG);
        } catch (IOException | SAXException e) {
            return;
        }
        assert roomPercentageCoordinates.size() == rooms.size();
        Map<String, Object> singleRoomInfo;
        for (int i = 0 ; i < rooms.size() ; i ++){
            singleRoomInfo = new HashMap<>();
            singleRoomInfo.put("id", rooms.get(i).getId());
            String roomName = rooms.get(i).getFloor() + String.format("%03d", i+1);
            singleRoomInfo.put("name", roomName);
            singleRoomInfo.put("category", rooms.get(i).getCategory());
            singleRoomInfo.put("coordinates", roomPercentageCoordinates.get(i));
            roomArr.add(singleRoomInfo);
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
