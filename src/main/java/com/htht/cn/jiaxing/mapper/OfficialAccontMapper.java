package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.OfficialAccountUserModel;
import com.htht.cn.jiaxing.model.dto.OfficialAccountUserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OfficialAccontMapper {

    @Select("select id,username,password,mobile from official_account_user where valid=1 and id = #{id}")
    OfficialAccountUserDTO selectById(long id) ;

    @Select("select id,username,password,mobile from official_account_user where valid=1 and username = #{username}")
    OfficialAccountUserDTO findUserByName(String username);

    @Insert("insert into official_account_user(username,password,valid,mobile) values(#{user.userName},#{user.password},1,#{user.mobile})")
    void insertOne(@Param("user") OfficialAccountUserModel user);
}
