package com.sustech.ooad.mapper.dataMappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void signUp();
}
