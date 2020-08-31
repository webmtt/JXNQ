package com.htht.cn.jiaxing.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlantCoverRateDTO {
private Long id;//
private Long year;//
private String season;// '季度',
private Float rate;//'覆盖率',
private String crop;// '作物种类 水稻rice   油菜rape  麦子wheat',
}
