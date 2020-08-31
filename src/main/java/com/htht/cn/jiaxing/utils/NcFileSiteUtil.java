package com.htht.cn.jiaxing.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;

public class NcFileSiteUtil {

    public static String NcFileSavePath = "/jxnq/data/nc/";

    /**
     * 获取日期的DAY值
     *
     * @param
     * @return
     */
    public static String getSite() {

        //String a = "20200101100000";
        //Date date = DateUtils.stringToDate(a,"yyyyMMddHHmmss");

        Date date = new Date();
        int hourOfDate = DateUtils.getHourOfDate(date);
        int dayOfDate = DateUtils.getDayOfDate(date);
        int monthOfDate = DateUtils.getMonthOfDate(date);
        int yearOfDate = DateUtils.getYearOfDate(date);
        String dayDir = String.format("%02d", dayOfDate);
        String monthDir = String.format("%02d", monthOfDate);
        String yearDir = String.format("%04d", yearOfDate);

        Date date1 = DateUtils.addDay(date, -1);
        int dayOfDate1 = DateUtils.getDayOfDate(date1);
        int monthOfDate1 = DateUtils.getMonthOfDate(date1);
        int yearOfDate1 = DateUtils.getYearOfDate(date1);
        String dayDir1 = String.format("%02d", dayOfDate1);
        String monthDir1 = String.format("%02d", monthOfDate1);
        String yearDir1 = String.format("%04d", yearOfDate1);

        String fileName = "";
        if (hourOfDate>10){
            fileName = yearDir+monthDir+dayDir+"10.nc";
        }else {
            fileName = yearDir1+monthDir1+dayDir1+"10.nc";
        }

        return NcFileSavePath+fileName;
    }

}
