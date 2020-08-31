package com.htht.cn.jiaxing.utils.wCounter.global;

import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Properties;

@Slf4j
@Component
public class ReadGeoInfo implements ApplicationRunner {
    /**
     * Created by ubuntu on 2015/7/10 0010.
     */
    @Value("${user.isGdalOpen}")
    private String flag;

    @Value("${tiff.mask-path}")
    private String path;

    public static float[][] maskArr = null;

    public float[][] readTiff(String path) throws Exception {
        if(!"Y".equals(flag)) {
            //因为gdal环境问题,如果是本地 则默认关闭
            return null;
        }
        Properties properties = System.getProperties();
        log.info("-------------------------"+System.getProperty("java.library.path"));

       // String fileName_tif =  "D:/workspace/JXNQ/target/classes/jiaxing_mask.tif";
        gdal.AllRegister();
        Dataset hDataset = gdal.Open(path, gdalconstConstants.GA_ReadOnly);
        if (hDataset == null) {
            System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            System.exit(1);
            throw new  Exception("未找到Dataset");
        }
        Driver hDriver = hDataset.GetDriver();
        log.info("Driver: " + hDriver.getShortName() + "/" + hDriver.getLongName());
        int iXSize = hDataset.getRasterXSize();
        int iYSize = hDataset.getRasterYSize();
        log.info("Size is " + iXSize + ", " + iYSize);
        Band band = hDataset.GetRasterBand(1);
        float res[][] = new float[iXSize][iYSize];
        int x = 0;
        for (int i = 0; i < iYSize; i++) {
            float buf[] = new float[iXSize];
            band.ReadRaster(0, i, iXSize, 1, buf);
            for (int j = 0; j < iYSize; j++){
                res[i][j]=buf[j];
            }
        }
        hDataset.delete();
        return res;
        }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        URL url = ReadGeoInfo.class.getClassLoader().getResource("jiaxing_mask.tif");
        String fileName_tif =  url.getPath().replaceFirst("/D:","D:");
        //String fileName_tif =  "D:/workspace/JXNQ/target/classes/jiaxing_mask.tif";
        maskArr = readTiff(fileName_tif);
        log.info("finished read mask tiff:"+fileName_tif);
    }
}