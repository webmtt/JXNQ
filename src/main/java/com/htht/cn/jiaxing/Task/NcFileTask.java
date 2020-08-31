package com.htht.cn.jiaxing.Task;

import com.htht.cn.jiaxing.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

@Component
public class NcFileTask {

    @Value("${ncFileUrl}")
    private String ncFileUrl;

    @Value("${ncFileSavePath}")
    private String ncFileSavePath;

    private Logger logger = LoggerFactory.getLogger(NcFileTask.class);

    @Scheduled(cron = "0 20-30 10 * * ?")
    public void getNcFile() throws Exception {
        logger.info("===nc file start===");

        Date date = new Date();
        int dayOfDate = DateUtils.getDayOfDate(date);
        int monthOfDate = DateUtils.getMonthOfDate(date);
        int yearOfDate = DateUtils.getYearOfDate(date);
        String dayDir = String.format("%02d", dayOfDate);
        String monthDir = String.format("%02d", monthOfDate);
        String yearDir = String.format("%04d", yearOfDate);

        String fileName = yearDir+monthDir+dayDir+"10.nc";
        String midPath = yearDir+"."+monthDir;

        String httpUrl = ncFileUrl+midPath+"/"+yearDir+monthDir+dayDir+"/"+fileName;
        String savePath = ncFileSavePath;
        String filePath = savePath+"/"+fileName;

        System.out.println(httpUrl);
        System.out.println(filePath);

        File file = new File(filePath);
        if (file.exists()){
            logger.info("===today nc file succeed===");
        }else {
            // 构造URL
            URL url = new URL(httpUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            OutputStream os = new FileOutputStream(sf.getPath() + "/" + fileName);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            logger.info("===nc file stop===");
        }

    }

    @Scheduled(cron = "0 20-30 10 * * ?")
    public void deleteNcFile() throws Exception {
        Date date = new Date();
        Date date1 = DateUtils.addDay(date, -2);
        int dayOfDate1 = DateUtils.getDayOfDate(date1);
        int monthOfDate1 = DateUtils.getMonthOfDate(date1);
        int yearOfDate1 = DateUtils.getYearOfDate(date1);
        String dayDir1 = String.format("%02d", dayOfDate1);
        String monthDir1 = String.format("%02d", monthOfDate1);
        String yearDir1 = String.format("%04d", yearOfDate1);

        String fileName = yearDir1+monthDir1+dayDir1+"10.nc";
        String filePath = ncFileSavePath+"/"+fileName;
        System.out.println(filePath);
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info(fileName+"文件删除成功");
            }else {
                logger.info(fileName+"文件删除失败");
            }
        }else {
            logger.info(fileName+"文件不存在");
        }
    }


}
