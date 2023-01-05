package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.User;
import org.apache.ibatis.annotations.Mapper;


public interface UserMapper extends BaseMapper<User> {

    void signUpInsert(User user);

    void setPasswdById(Integer id, String password);

    User getUserByMail(String mail);

    String getFavoritesById(Integer userId);

    User getUserById(Integer userId);

    void setFavoritesById(Integer userId, String favorites);
}
