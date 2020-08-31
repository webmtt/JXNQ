package com.htht.cn.jiaxing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ImageDisplayVO {
    private Long id;
    //影像源
    private String imageSrc;
    //总体描述
    private String imageDesc;
    //影像分析
    private String imageAnalysis;
    //作物种植指导
    private String guide;
}
