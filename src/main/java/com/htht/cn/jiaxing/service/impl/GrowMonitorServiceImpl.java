package com.htht.cn.jiaxing.service.impl;

import com.htht.cn.jiaxing.constant.Consts;
import com.htht.cn.jiaxing.mapper.ImageDisplayMapper;
import com.htht.cn.jiaxing.mapper.PlantCoverRateMapper;
import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;
import com.htht.cn.jiaxing.model.dto.ImageDisplayDTO;
import com.htht.cn.jiaxing.model.dto.PlantCoverRateDTO;
import com.htht.cn.jiaxing.service.GrowMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GrowMonitorServiceImpl implements GrowMonitorService {

    private static String[] seasonStr = new String[] {"一季度","二季度","三季度","四季度"};

    @Autowired
    ImageDisplayMapper imageDisplayMapper;

    @Autowired
    PlantCoverRateMapper plantCoverRateMapper;

    @Override
    public ImageDisplayVO imageQuery(String imageName, String plantCover) {

        ImageDisplayVO imageDisplayVO = imageDisplayMapper.imageQuery(imageName, plantCover);
        Long id = null;
        if(null == imageDisplayVO) {
            //插入一个只有图片名的记录
            ImageDisplayDTO imageDisplayDTO = new ImageDisplayDTO();
            imageDisplayDTO.setImageName(imageName);
            imageDisplayDTO.setType(plantCover);
            int i = imageDisplayMapper.imageInsert(imageDisplayDTO);
            id = imageDisplayDTO.getId();
            imageDisplayVO = new ImageDisplayVO();
            imageDisplayVO.setId(id);
        }
        return imageDisplayVO;
    }

    @Override
    public int updateImageInfo(@Validated ImageInfoModel imageInfoModel) {
        imageInfoModel.setType(Consts.GROW_MONITOR);
        return imageDisplayMapper.updateImageInfo(imageInfoModel);
    }

    @Override
    public Map<Object, Object> rateQuery() {
        List<PlantCoverRateDTO> plantCoverRateDTOS = plantCoverRateMapper.selectList();
        Map<Object,Object> res = new LinkedHashMap<>();
        if(plantCoverRateDTOS == null || plantCoverRateDTOS.size()==0) {

        }else {

            Map<Long, List<PlantCoverRateDTO>> collect = plantCoverRateDTOS.stream().collect(Collectors.groupingBy(PlantCoverRateDTO::getYear));
            Set<Long> longs = collect.keySet();
            for (Long aLong : longs) {
                List<Float> rateInner = new ArrayList<>();
                for (int i = 0, size = seasonStr.length; i < size; i++) {
                    String s = seasonStr[i];
                    List<PlantCoverRateDTO> list = collect.get(aLong);
                    for (PlantCoverRateDTO plantCoverRateDTO : list) {
                        if (plantCoverRateDTO.getSeason().equals(s)) {
                            rateInner.add(i,plantCoverRateDTO.getRate());
                            break;
                        }
                    }
                }
                res.put(aLong,rateInner);
            }
            res.put("season",seasonStr);
        }
        return res;
    }
}
