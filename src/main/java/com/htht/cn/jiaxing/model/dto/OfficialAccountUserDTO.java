package com.htht.cn.jiaxing.model.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class OfficialAccountUserDTO {
    private Long id;
    private String userName;
    @JsonIgnore
    private String password;
    private String mobile;
    @JsonIgnore
    private Integer valid;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Date updatedDate;
}
