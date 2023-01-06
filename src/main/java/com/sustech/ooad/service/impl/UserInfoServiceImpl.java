package com.sustech.ooad.service.impl;

import com.sustech.ooad.Utils.JWTUtils;
import com.sustech.ooad.entity.data.Customer;
import com.sustech.ooad.entity.data.Order;
import com.sustech.ooad.entity.data.Review;
import com.sustech.ooad.entity.data.User;
import com.sustech.ooad.mapper.dataMappers.*;
import com.sustech.ooad.service.CustomerAccountService;
import com.sustech.ooad.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    ReviewMapper reviewMapper;
    @Override
    public void getUserInfoByJWT(Map<String, String> requestInfo, Map<String, Object> userInfoResponse) {
        Map<String, String> JWTCheckResponse = new HashMap<>();
        String JWTToken = requestInfo.get("jwt");
        if (JWTToken == null)
            return;
        customerAccountService.checkJWT(JWTToken, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1")){
            return;
        }
        String userId = String.valueOf(JWTUtils.decode(JWTToken).getClaim("user_id"));
        userId = userId.replaceAll("\"","");
        Customer customer = customerMapper.getCustomerById(Integer.valueOf(userId));
        User user = userMapper.getUserById(Integer.valueOf(userId));
        userInfoResponse.put("firstname", user.getFirstname());
        userInfoResponse.put("lastname", user.getLastname());
        userInfoResponse.put("avatar", user.getAvatar());
        userInfoResponse.put("prefix", customer.getNamePrefix());
        userInfoResponse.put("suffix", customer.getNameSuffix());
        userInfoResponse.put("phone", customer.getPhoneNumber());
        userInfoResponse.put("phoneCode", customer.getPhoneHead());
        userInfoResponse.put("mail", user.getMail());
        userInfoResponse.put("birthday", customer.getBirthday());
        userInfoResponse.put("gender", customer.getGender());
        userInfoResponse.put("points", customer.getPoints());
        userInfoResponse.put("country", customer.getCountry());
    }

    @Override
    public void getUserStatisticsByJWT(Map<String, String> requestInfo, Map<String, Object> userStatisticsResponse) {
        Map<String, String> JWTCheckResponse = new HashMap<>();
        String JWTToken = requestInfo.get("jwt");
        if (JWTToken == null)
            return;
        customerAccountService.checkJWT(JWTToken, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1")){
            return;
        }
        String userId = String.valueOf(JWTUtils.decode(JWTToken).getClaim("user_id"));
        userId = userId.replaceAll("\"","");
        Customer customer = customerMapper.getCustomerById(Integer.valueOf(userId));
        User user = userMapper.getUserById(Integer.valueOf(userId));
        if (user.getRole() == 2)
            userStatisticsResponse.put("totalSales", orderMapper.getOrderPriceSum());
    }

    @Override
    public void getUserDataByJWT(Map<String, String> requestInfo, List<Object> userDataResponse) {
        Map<String, String> JWTCheckResponse = new HashMap<>();
        String JWTToken = requestInfo.get("jwt");
        if (JWTToken == null)
            return;
        customerAccountService.checkJWT(JWTToken, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1")){
            return;
        }
        String userId = String.valueOf(JWTUtils.decode(JWTToken).getClaim("user_id"));
        userId = userId.replaceAll("\"","");
        User user = userMapper.getUserById(Integer.valueOf(userId));
        if (user.getRole() != 1)
            return;
        List<Order> orders = orderMapper.getOrderByUserId(Integer.valueOf(userId));
        Map<String, Object> singleOrderInfo;
        for (Order order : orders) {
            singleOrderInfo = new HashMap<>();
            singleOrderInfo.put("number", String.format("%016d", order.getId()));
            Map<String, Date> orderDatePair = new HashMap<>();
            orderDatePair.put("start", order.getStartDate());
            orderDatePair.put("end", order.getEndDate());
            singleOrderInfo.put("date", orderDatePair);
            singleOrderInfo.put("hotel", hotelMapper.getHotelById(order.getHotelId()).getName());
            singleOrderInfo.put("room",roomMapper.getRoomById(order.getRoomId()).getName());
            Review review = reviewMapper.getReviewByOrderId(order.getId());
            singleOrderInfo.put("reviewed", review != null);
            userDataResponse.add(singleOrderInfo);
        }
    }

}
