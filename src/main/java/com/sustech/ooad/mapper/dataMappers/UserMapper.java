package com.sustech.ooad.mapper.dataMappers;

import com.sustech.ooad.entity.data.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    void signUpInsert(User user);

    void setPasswdByID(Integer id, String password);

    User getUserByMail(String mail);
}
