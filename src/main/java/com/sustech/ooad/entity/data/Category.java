package com.sustech.ooad.entity.data;

import lombok.Data;

@Data
public class Category {
    Integer id;
    Integer hotelId;
    Integer maxPeople;
    Integer maxChildren;
    String availableRates;
    Boolean accessible;
    String name;
    Integer price;
    Integer points;
    String description;
    String amenities;
    Boolean picture;
    String prefix;
    String currency;
}
