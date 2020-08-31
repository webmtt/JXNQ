package com.htht.cn.jiaxing.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.htht.cn.jiaxing.mapper.LoginAndRegister;
import com.htht.cn.jiaxing.mapper.UserDao;
import com.htht.cn.jiaxing.model.User;
import com.htht.cn.jiaxing.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private LoginAndRegister loginAndRegister;

    @Override
    public Object findAll(int page,int size) {
        PageHelper.startPage(page, size);
        List<User> userList = userDao.selectAll();
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return pageInfo;
    }

    @Override
    public List<User> findById(List<Long> list) {
        return userDao.selectById(list);
    }

    /*
    @Override
    public String find1ByName(String user_name){
        return userDao.select1ByName(user_name);
    }

    @Override
    public Integer find2ByName(String user_name){
        return userDao.select2ByName(user_name);
    }
    */


    @Override
    public boolean addUser(User user) {
        return userDao.insert(user);
    }

    @Override
    public boolean setUser(User user) {
        return userDao.update(user);
    }

    @Override
    public boolean setPwd(String user_id, String user_password) {
       return userDao.updatePwd(user_id, user_password);
    }

    @Override
    public boolean deleteUser(List<Long> user_id) {
        return userDao.deleteById(user_id);
    }



    /*
    @Override
    public User userLogin(String user_name, String user_password) {
        return loginAndRegister.login(user_name,user_password);
    }
    */

    @Override
    public Integer userRegister(User user) {
        return loginAndRegister.register(user);
    }

    @Override
    public Integer findName(String user_name){
        return loginAndRegister.selectByName(user_name);
    }

    @Override
    public User uLogin(String user_name, String user_password) {
        return userDao.uLogin(user_name,user_password);
    }
}
