package com.sustech.ooad.mapper.dataMappers;

import com.sustech.ooad.entity.data.Customer;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;


public interface CustomerMapper {
    Customer getCustomerByMail(String mail);
    void signUpInsert(Customer customer);
}
