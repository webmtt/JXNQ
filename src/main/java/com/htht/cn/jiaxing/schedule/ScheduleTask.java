package com.htht.cn.jiaxing.schedule;

import com.htht.cn.jiaxing.mapper.ImageInfoMapper;
import com.htht.cn.jiaxing.mapper.WeatherDataMapper;
import com.htht.cn.jiaxing.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    WeatherDataMapper weatherDataMapper;

    @Autowired
    ImageInfoMapper imageInfoMapper;

    @Value("${spring.http.multipart.locations}")
    String imagePath;

    //删除气象服务中 一个月前的数据
    //包括 气象站信息,生成的png图片
    @Scheduled(cron = "0 30 1 * */1  ?")
    public void del() {
        log.info("start task: del weather service data out of date --------");
        Date date = new Date();
        Date addDay = DateUtils.addDay(date, -30);
        weatherDataMapper.deleteMonthAgo(addDay);
        imageInfoMapper.deleteMonthAgo(addDay);
        String string = DateUtils.dateToString(new Date(),"yyyy-MM-dd-HH");
        File file = new File(imagePath+"/gdal");
        File[] listFiles = file.listFiles();
        for (File listFile : listFiles) {
            String name = listFile.getName();
            try {
                Date date1 = DateUtils.stringToDate(name, "yyyy-MM-dd-HH");
                if(date1.before(addDay)){
                    delFile(listFile);
                }
            }catch (Exception e) {
                log.error("",e);
            }
        }
        log.info("finished task: del weather service data out of date --------");

    }

    //递归删除一个月前的文件夹
    private void delFile(File file) {
        if(file.isFile()) {
            file.delete();
        }else {
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                delFile(listFile);
            }
            file.delete();
        }
    }
}
