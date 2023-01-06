package com.sustech.ooad.service.impl;

import com.sustech.ooad.Utils.JWTUtils;
import com.sustech.ooad.entity.data.*;
import com.sustech.ooad.mapper.dataMappers.*;
import com.sustech.ooad.property.PricingProp;
import com.sustech.ooad.service.CustomerAccountService;
import com.sustech.ooad.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    OfferMapper offerMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    CustomerAccountService customerAccountService;
    @Override
    public void orderPurchase(Map<String, String> requestInfo, Map<String, Object> orderPurchaseResult) {
        String JWT = requestInfo.get("customer");
        String start = requestInfo.get("start");
        String end = requestInfo.get("end");
        String hotelId = requestInfo.get("hotel");
        String roomId = requestInfo.get("room");
        String categoryId = requestInfo.get("category");
        String rate = requestInfo.get("rate");
        String offer = requestInfo.get("offer");
        String people = requestInfo.get("people");
        String children = requestInfo.get("children");
        if(JWT == null || start == null || end == null || hotelId == null || roomId == null
                || categoryId == null || rate == null || people == null || children == null){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "exists field in request is null");
            return;
        }
        offer = offer == null ? "" : offer;
        Map<String, String> JWTCheckResponse = new HashMap<>();
        customerAccountService.checkJWT(JWT, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1")){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "user authentication failed");
            return;
        }
        String userId = String.valueOf(JWTUtils.decode(JWT).getClaim("user_id"));
        userId = userId.replaceAll("\"", "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sqlStartDate, sqlEndDate;
        try {
            sqlStartDate = new Date(dateFormat.parse(start).getTime());
            sqlEndDate = new Date(dateFormat.parse(end).getTime());
        } catch (ParseException e) {
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "date format invalid");
            return;
        }
        boolean dateValid = orderMapper.getOrderConflictedCntByDateAndRoomId(
                sqlStartDate, sqlEndDate, Integer.valueOf(roomId)) == 0;
        if (!dateValid){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "date conflicts with existing order");
            return;
        }
        Room room = roomMapper.getRoomById(Integer.valueOf(roomId));
        if (room == null){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "room id invalid");
            return;
        }
        int intRate = Integer.parseInt(rate);
        double purchasePrice = Double.POSITIVE_INFINITY;
        Category category = categoryMapper.getCategoryById(Integer.valueOf(categoryId));
        Offer roomOffer = null;
        if (intRate == 0)
            purchasePrice = category.getPrice() * PricingProp.STANDARD_RATE;
        else if (intRate == 1)
            purchasePrice = category.getPrice() * PricingProp.STUDENT_RATE;
        else if (intRate == 2)
            purchasePrice = category.getPrice() * PricingProp.MILITARY_RATE;
        else if (intRate == 3){
             roomOffer = offerMapper.getOfferByCode(offer);
            if (roomOffer == null){
                orderPurchaseResult.put("code", -1);
                orderPurchaseResult.put("message", "offer code not exists");
                return;
            }
            purchasePrice = category.getPrice() * roomOffer.getRatio();
        }
        Customer customer = customerMapper.getCustomerById(Integer.valueOf(userId));
        if(customer.getPoints()*PricingProp.POINTS_RATIO < purchasePrice){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "insufficient points");
            return;
        }
        if(Integer.parseInt(children) > category.getMaxChildren()){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "children number exceeded");
            return;
        }
        if(Integer.parseInt(people) > category.getMaxPeople()){
            orderPurchaseResult.put("code", -1);
            orderPurchaseResult.put("message", "people number exceeded");
            return;
        }

        orderMapper.placeOrder(new Order(
                0,
                Integer.parseInt(userId),
                purchasePrice,
                Integer.parseInt(hotelId),
                Integer.parseInt(roomId),
                sqlStartDate, sqlEndDate,
                Integer.parseInt(people),
                Integer.parseInt(children),
                0,
                false
        ));
        customerMapper.setPointsById(
                Integer.valueOf(userId),
                Math.max(0, (int)(customer.getPoints() - purchasePrice / PricingProp.POINTS_RATIO))
        );
        orderPurchaseResult.put("code", 0);
        orderPurchaseResult.put("message", "success");

    }
}
