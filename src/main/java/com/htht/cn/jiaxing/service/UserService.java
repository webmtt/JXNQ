package com.htht.cn.jiaxing.service;



import com.htht.cn.jiaxing.model.User;

import javax.imageio.stream.IIOByteBuffer;
import java.util.List;

public interface UserService {

    /**
     * 增删改查
     * @return
     */
    Object findAll(int page,int size);

    List<User> findById(List<Long> list);

    //String find1ByName(String user_name);

    //Integer find2ByName(String user_name);

    boolean addUser(User user);

    boolean setUser(User user);

    boolean setPwd(String user_id,String user_password);

    boolean deleteUser(List<Long> user_id);


    /**
     * 登录注册
     */

    //User userLogin (String user_name,String user_password);

    //注册
    Integer userRegister(User user);

    //用户名查重
    Integer findName(String user_name);

    //登录
    User uLogin(String user_name,String user_password);


}
