package com.htht.cn.jiaxing.mapper;


import com.htht.cn.jiaxing.model.User;

import java.util.List;

public interface LoginAndRegister {

    //登录
    User login(String user_name, String user_password);

    //注册
    Integer register(User user);

    //用户名查重
    Integer selectByName(String user_name);
}
