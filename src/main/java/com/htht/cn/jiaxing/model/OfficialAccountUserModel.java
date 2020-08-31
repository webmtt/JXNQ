package com.htht.cn.jiaxing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class OfficialAccountUserModel {
    private String userName;
    private String password;
    private String mobile;
}
