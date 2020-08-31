package com.htht.cn.jiaxing.model;

import com.htht.cn.jiaxing.model.dto.PlantCoverRateDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CoverRateVO {
    private Integer year;
    private String[] season;
    private Map rate;
}

