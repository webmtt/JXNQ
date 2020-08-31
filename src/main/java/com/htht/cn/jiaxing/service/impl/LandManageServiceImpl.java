package com.htht.cn.jiaxing.service.impl;

import com.htht.cn.jiaxing.mapper.CropCultivationYearMsgDao;
import com.htht.cn.jiaxing.model.CropCultivationYearMsg;
import com.htht.cn.jiaxing.service.LandManageService;
import com.htht.cn.jiaxing.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LandManageServiceImpl implements LandManageService {

    @Autowired
    CropCultivationYearMsgDao cropCultivationYearMsgDao;

    @Override
    public List<CropCultivationYearMsg> query(String district, String year, String cropBreeds) {
        return cropCultivationYearMsgDao.query(district, year, cropBreeds);
    }

    /*@Override
    public Map getCropByDistrict(String district, String year) {
        List<CropCultivationYearMsg> rices = cropCultivationYearMsgDao.query(district, year, "rice");
        List<CropCultivationYearMsg> rapes = cropCultivationYearMsgDao.query(district, year, "rape");
        List<CropCultivationYearMsg> wheats = cropCultivationYearMsgDao.query(district, year, "wheat");
        float riceA = 0;
        float rapeA = 0;
        float wheatA = 0;
        for (CropCultivationYearMsg crop : rices) {
            Float area = crop.getCultivationArea();
            riceA = riceA + area;
        }
        for (CropCultivationYearMsg crop : rapes) {
            Float area = crop.getCultivationArea();
            rapeA = rapeA + area;
        }
        for (CropCultivationYearMsg crop : wheats) {
            Float area = crop.getCultivationArea();
            wheatA = wheatA + area;
        }
        Map map = new HashMap();
        map.put("rices", riceA);
        map.put("rapes", rapeA);
        map.put("wheats", wheatA);
        return map;
    }*/

    @Override
    public Map getCropByDistrict(String district, String year) {
        Float rices = cropCultivationYearMsgDao.querySumByDistrict(district, year, "rice");
        Float rapes = cropCultivationYearMsgDao.querySumByDistrict(district, year, "rape");
        Float wheats = cropCultivationYearMsgDao.querySumByDistrict(district, year, "wheat");
        Map map = new HashMap();
        map.put("rices", rices);
        map.put("rapes", rapes);
        map.put("wheats", wheats);
        return map;
    }

    /*@Override
    public List<Map> cropLineChart(String district) {
        List<Map> list = new ArrayList<>();
        Date date = new Date();
        String year = DateUtils.dateToString(date, "yyyy");
        int maxYear = Integer.parseInt(year);
        int minYear = maxYear - 7;
        List<CropCultivationYearMsg> rices = cropCultivationYearMsgDao.cropLineChart(district, maxYear, minYear, "rice");
        List<CropCultivationYearMsg> rapes = cropCultivationYearMsgDao.cropLineChart(district, maxYear, minYear, "rape");
        List<CropCultivationYearMsg> wheats = cropCultivationYearMsgDao.cropLineChart(district, maxYear, minYear, "wheat");
        float[] riceA = new float[maxYear-minYear+1];
        for (int i = 0; i < rices.size(); i++) {
            for (int j = minYear; j <maxYear+1 ; j++) {
                if (rices.get(i).getYear().equals(String.valueOf(j))){
                    float area = rices.get(i).getCultivationArea();
                    System.out.println(area);
                    riceA[j-minYear] = riceA[j-minYear] + area;
                }
            }
        }
        float[] rapeA = new float[maxYear-minYear+1];
        for (int i = 0; i < rapes.size(); i++) {
            for (int j = minYear; j <maxYear+1 ; j++) {
                if (rapes.get(i).getYear().equals(String.valueOf(j))){
                    float area = rapes.get(i).getCultivationArea();
                    rapeA[j-minYear] = rapeA[j-minYear] + area;
                }
            }
        }
        float[] wheatA = new float[maxYear-minYear+1];
        for (int i = 0; i < wheats.size(); i++) {
            for (int j = minYear; j <maxYear+1 ; j++) {
                if (wheats.get(i).getYear().equals(String.valueOf(j))){
                    float area = wheats.get(i).getCultivationArea();
                    wheatA[j-minYear] = wheatA[j-minYear] + area;
                }
            }
        }

        Map riceMap = new HashMap();
        riceMap.put("rice", riceA);
        list.add(riceMap);

        Map rapeMap = new HashMap();
        rapeMap.put("rape", rapeA);
        list.add(rapeMap);

        Map wheatMap = new HashMap();
        wheatMap.put("wheat", wheatA);
        list.add(wheatMap);

        String[] y = new String[maxYear-minYear+1];
        for (int a = minYear; a < maxYear+1; a++) {
            y[a-minYear]=String.valueOf(a);
        }
        Map yearMap = new HashMap();
        yearMap.put("year", y);
        list.add(yearMap);

        return list;

    }*/

    @Override
    public List<Map> cropLineChart(String district) {
        List<Map> list = new ArrayList<>();
        Date date = new Date();
        String year = DateUtils.dateToString(date, "yyyy");
        int maxYear = Integer.parseInt(year);
        int minYear = maxYear - 7;

        float[] riceA = new float[maxYear-minYear+1];
        for (int i = minYear; i <= maxYear; i++) {
            Float rices = cropCultivationYearMsgDao.querySumByDistrict(district, String.valueOf(i), "rice");
            if (rices == null){
                riceA[i-minYear] = 0 ;
            }else {
                riceA[i-minYear] = rices ;
            }

        }
        float[] rapeA = new float[maxYear-minYear+1];
        for (int i = minYear; i <= maxYear; i++) {
            Float rapes = cropCultivationYearMsgDao.querySumByDistrict(district, String.valueOf(i), "rape");
            if (rapes == null){
                rapeA[i-minYear] = 0 ;
            }else {
                rapeA[i-minYear] = rapes ;
            }
        }
        float[] wheatA = new float[maxYear-minYear+1];
        for (int i = minYear; i <= maxYear; i++) {
            Float wheats = cropCultivationYearMsgDao.querySumByDistrict(district, String.valueOf(i), "wheat");
            if (wheats == null){
                wheatA[i-minYear] = 0 ;
            }else {
                wheatA[i-minYear] = wheats ;
            }
        }

        Map riceMap = new HashMap();
        riceMap.put("rice", riceA);
        list.add(riceMap);

        Map rapeMap = new HashMap();
        rapeMap.put("rape", rapeA);
        list.add(rapeMap);

        Map wheatMap = new HashMap();
        wheatMap.put("wheat", wheatA);
        list.add(wheatMap);

        String[] y = new String[maxYear-minYear+1];
        for (int a = minYear; a < maxYear+1; a++) {
            y[a-minYear]=String.valueOf(a);
        }
        Map yearMap = new HashMap();
        yearMap.put("year", y);
        list.add(yearMap);

        return list;

    }

}
