package com.htht.cn.jiaxing.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel(value = "图新描述信息模型")
public class ImageInfoModel {
    @NotNull(message = "图片id不得为空")
    private Long id;
    //影像源
    @ApiModelProperty(value="图像源" ,required=false)
    private String imageSrc;
    //总体描述
    @ApiModelProperty(value="图像描述" ,required=false)
    private String imageDesc;
    //影像分析
    @ApiModelProperty(value="图像分析" ,required=false)
    private String imageAnalysis;
    //作物种植指导
    @ApiModelProperty(value="图像指导" ,required=false)
    private String guide;
    //长势还是植被
    @ApiModelProperty(value="类型" ,required=false)
    private String type;
}
