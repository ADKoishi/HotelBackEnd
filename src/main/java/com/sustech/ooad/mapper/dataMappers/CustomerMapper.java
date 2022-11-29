package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Customer;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;


public interface CustomerMapper extends BaseMapper<Customer> {
    Customer getCustomerByMail(String mail);
    void signUpInsert(Customer customer);
}
