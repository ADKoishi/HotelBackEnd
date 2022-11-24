package com.sustech.ooad.mapper.dataMappers;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface UserMapper {
    void signUp(String firstName, String lastName, String mail, String country, Date birthday);
}
