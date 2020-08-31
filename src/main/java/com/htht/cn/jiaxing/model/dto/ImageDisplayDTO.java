package com.htht.cn.jiaxing.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ImageDisplayDTO {
    private Long id;//主键',
    private String imageDesc;//'总体描述',
    private String imageSrc;//'影像源',
    private String imageAnalysis;//'影像分析',
    private String guide;//'作物种植指导',
    private String createdDate;// '创建时间',
    private String updatedDate;// '修改时间',
    private String type;//种类
    private String imageName;//种类
}
