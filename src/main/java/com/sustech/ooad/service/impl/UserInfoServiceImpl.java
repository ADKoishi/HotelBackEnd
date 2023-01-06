package com.sustech.ooad.service.impl;

import com.sustech.ooad.Utils.JWTUtils;
import com.sustech.ooad.entity.data.*;
import com.sustech.ooad.mapper.dataMappers.*;
import com.sustech.ooad.property.PricingProp;
import com.sustech.ooad.service.CustomerAccountService;
import com.sustech.ooad.service.UserInfoService;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        userInfoResponse.put("firstName", user.getFirstname());
        userInfoResponse.put("lastName", user.getLastname());
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

    @Override
    public void cancelOrder(Map<String, String> requestInfo, Map<String, Object> cancelResponse) {
        Map<String, String> JWTCheckResponse = new HashMap<>();
        String JWTToken = requestInfo.get("jwt");
        String orderNumber = requestInfo.get("number");
        if (JWTToken == null || orderNumber == null)
            return;
        customerAccountService.checkJWT(JWTToken, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1")){
            return;
        }
        String userId = String.valueOf(JWTUtils.decode(JWTToken).getClaim("user_id"));
        userId = userId.replaceAll("\"","");
        Order order = orderMapper.getOrderById(Integer.parseInt(orderNumber));
        if (order == null){
            cancelResponse.put("code", -1);
            cancelResponse.put("message", "order not exists");
            return;
        }
        if (!userId.equals(String.valueOf(order.getUserId()))){
            cancelResponse.put("code", -1);
            cancelResponse.put("message", "target order's owner not match");
            return;
        }
        if (order.getStartDate().toLocalDate().isBefore(LocalDate.now())){
            cancelResponse.put("code", -1);
            cancelResponse.put("message", "order already started, cancel failed");
            return;
        }
        Customer customer = customerMapper.getCustomerById(Integer.valueOf(userId));
        customerMapper.setPointsById(
                customer.getId(),(int) (customer.getPoints() + order.getPrice() / PricingProp.POINTS_RATIO));
        orderMapper.moveOrderToDeletedById(order.getId());
        orderMapper.deleteAvailOrder(order.getId());
        cancelResponse.put("code", 0);
        cancelResponse.put("message", "success");
    }

    @Override
    public void uploadReview(Map<String, String> requestInfo, Map<String, Object> uploadResponse) {
        Map<String, String> JWTCheckResponse = new HashMap<>();
        String JWTToken = requestInfo.get("jwt");
        String orderNumber = requestInfo.get("number");
        String language = requestInfo.get("language");
        String stars = requestInfo.get("stars");
        String postDate = requestInfo.get("postDate");
        String description = requestInfo.get("description");
        if (JWTToken == null || orderNumber == null || language == null || stars == null
            || postDate == null || description == null) {
            uploadResponse.put("code", -1);
            uploadResponse.put("review", -1);
            uploadResponse.put("message", "field not exists");
            return;
        }
        customerAccountService.checkJWT(JWTToken, JWTCheckResponse);
        if (!Objects.equals(JWTCheckResponse.get("code"), "1")){
            return;
        }
        String userId = String.valueOf(JWTUtils.decode(JWTToken).getClaim("user_id"));
        userId = userId.replaceAll("\"","");
        Review review = reviewMapper.getReviewByOrderId(Integer.parseInt(orderNumber));
        if (review != null){
            uploadResponse.put("code", -1);
            uploadResponse.put("review", -1);
            uploadResponse.put("message", "review already exists");
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlPostDate;
        try {
            sqlPostDate = new java.sql.Date(dateFormat.parse(postDate).getTime());
        } catch (ParseException e) {
            uploadResponse.put("code", -1);
            uploadResponse.put("message", "date format invalid");
            return;
        }
        review = new Review(
                0, Integer.parseInt(userId), Integer.parseInt(orderNumber),
                language, "", 0, Integer.parseInt(stars),
                sqlPostDate, null, description, false,
                false, true, 0
        );
        reviewMapper.insertReview(review);
        uploadResponse.put("code", 0);
        uploadResponse.put("review", review.getId());
        uploadResponse.put("message", "success");
    }

    @Override
    public void getReviews(Map<String, Object> reviewResponse) {
        List<Review> reviews = reviewMapper.getAllRevies();
        for (Review review : reviews) {
            User user = userMapper.getUserById(review.getUserId());
            List<String> names = new ArrayList<>();
            names.add(user.getFirstname());
            names.add(user.getLastname());
            reviewResponse.put("producer", names);
            Hotel hotel = hotelMapper.getHotelById(orderMapper.getOrderById(review.getOrderId()).getHotelId());
            reviewResponse.put("hotel", hotel.getName());
            reviewResponse.put("type", review.getType());
            reviewResponse.put("stars", review.getStars());
            reviewResponse.put("postDate", review.getPostDate());
            reviewResponse.put("description", review.getDescription());

        }
        
    }

}
