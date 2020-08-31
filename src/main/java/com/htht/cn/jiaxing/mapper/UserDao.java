package com.htht.cn.jiaxing.mapper;



import com.htht.cn.jiaxing.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserDao {

    List<User> selectAll();

    List<User> selectById(List<Long> list);

    String select1ByName(String user_name);

    Integer select2ByName(String user_name);

    boolean insert(User user);

    boolean update(User user);

    boolean updatePwd(@Param("user_id") String user_id, @Param("user_password") String user_password);

    boolean deleteById(List<Long> user_id);

    User uLogin(@Param("user_name")String user_name,@Param("user_password")String user_password);



}
