package com.htht.cn.jiaxing.utils.wCounter;


import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.htht.cn.jiaxing.mapper.ImageInfoMapper;
import com.htht.cn.jiaxing.mapper.StationInfoMapper;
import com.htht.cn.jiaxing.mapper.WeatherDataMapper;
import com.htht.cn.jiaxing.model.dto.ImageInfoSDTO;
import com.htht.cn.jiaxing.model.dto.StationInfoDTO;
import com.htht.cn.jiaxing.model.dto.WeatherDataDTO;
import com.htht.cn.jiaxing.utils.DateUtils;
import com.htht.cn.jiaxing.utils.TiffColor;
import com.htht.cn.jiaxing.utils.wCounter.global.ReadGeoInfo;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.osr.SpatialReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;

@Slf4j
@Component
public class GeneImage implements ApplicationRunner{

    private static final float LEFT_LON =120.295342f;
    private static final float RIGHT_LON =121.274652f;
    private static final float TOP_LAT =31.030258f;
    private static final float BOT_LAT =30.262318f;
    //像素块数 n*n
    private static final int BLOCKNUM =2000;

    private static Map<String,StationInfoDTO> stationInfoMap = new HashMap<>(256);

    @Autowired
    StationInfoMapper stationInfoMapper;

    @Autowired
    WeatherDataMapper weatherDataMapper;


    @Autowired
    ImageInfoMapper imageInfoMapper;


    @Value("${tiff.weather-data-uri}")
    String url;

    @Value("${spring.http.multipart.locations}")
    private String location;

  @Scheduled(cron = "${tiff.gene-cron}")
    public void init()  {
        log.info("开始拉取数据生成气象图片");
        List<ImageInfoSDTO> list = genePng();
        if(list.size()>0) {
            imageInfoMapper.insertBath(list);
        }
        log.info("生成气象图片任务已完成");
    }

    /**
     * zw
     * 生成png图片  返回图片信息
     * */
    public List<ImageInfoSDTO> genePng() {

        //默认已包含经纬度
        List<String> keys = new ArrayList<>();
        keys.add("空气温度");
        keys.add("10分钟平均风速");
        keys.add("降水");
        keys.add("相对湿度");
        List<Map> data = readTxt();
        double[] y = getArr(TOP_LAT, BOT_LAT, BLOCKNUM);
        double[] x = getArr(LEFT_LON, RIGHT_LON, BLOCKNUM);
        List<ImageInfoSDTO> list = new ArrayList<>();
         String virtualPath = location + "/gdal";
        for ( String key : keys) {
            String string = DateUtils.dateToString(new Date(),"yyyy-MM-dd-HH");
            mkdir(virtualPath+File.separator+string);

            String attrByKey = getAttrByKey(key);
            String imagePath = virtualPath+"/"+string  +"/"+attrByKey + ".png";
            log.info("start generate image:" + imagePath);
            write(imagePath,data,key,x,y);
            ImageInfoSDTO imageInfoSDTO = new ImageInfoSDTO();
            imageInfoSDTO.setImagePath("/gdal"+"/"+string  +"/"+attrByKey + ".png");
            imageInfoSDTO.setAttr(getAttrByKey(key));
            String time = (String) data.get(0).get("时间");
            imageInfoSDTO.setTime(time.substring(0,time.length()-2));
            list.add(imageInfoSDTO);
        }
        return list;
    }

    private String getAttrByKey(String key) {
        //雨 rain 压 wind 温 temp 湿 humi",
        if("降水".equals(key)) {
            return "rain";
        }
        if("10分钟平均风速".equals(key)) {
            return "wind";
        }
        if("空气温度".equals(key)) {
            return "temp";
        }
        if("相对湿度".equals(key)) {
            return "humi";
        }
        return "-";
    }

    private void mkdir(String scanPath) {
        /**
         * 创建文件并创建文件夹
         */
        File file = new File(scanPath);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.setWritable(true, false);
                file.mkdirs();
            } catch (Exception e) {
                log.error("",e);
            }
        }
    }

    public  void write(String imagePath, List<Map> data, String key, double[] x, double[] y) {

        double[][] doubles = getDataArray(data,key);
        log.debug("获取数据如下:" + JSON.toJSONString(doubles));
        //生成格栅数据
        double[][] cressman = Interpolate.cressman(doubles, x, y, 999999);
        double[][] cressman2 = Interpolate.interpolation_IDW_Neighbor(doubles, x, y, 4,999999);
        gdal(imagePath,x,y,cressman,cressman2,key);
    }

    private static double[][] getDataArray(List<Map> data, String key) {
        double[][] doubles = new double[data.size()][3];
        for (int i = 0; i < data.size(); i++) {

            double [] d = new double[3];

            try {
                d[0] = Double.valueOf((Float) data.get(i).get("Lon"));
                d[1] = Double.valueOf((Float) data.get(i).get("Lat"));
                String s = (String) data.get(i).get(key);
                if (null == s) {
                    d[2] = 999999;

                } else if("空气温度".equals(key)){
                    String trim = s.replace("℃", "").trim();
                    d[2] = Double.valueOf(trim);
                }else if("降水".equals(key)) {
                    String trim = s.replace("mm", "").trim();
                    d[2] = Double.valueOf(trim);
                }else if("相对湿度".equals(key)) {
                    String trim = s.replace("%", "").trim();
                    d[2] = Double.valueOf(trim);
                }else if("10分钟平均风速".equals(key)) {
                    String trim = s.replace("m/s", "").trim();
                    d[2] = Double.valueOf(trim);
                }
            }catch (Exception e) {
                log.error("wrong happend." + JSON.toJSONString( data.get(i)));
                d[2] = 999999;
            }
            doubles[i]=d;


        }
        return doubles;
    }



    private  void gdal(String imagePath, double[] x, double[] y, double[][] cressman, double[][] cressman2, String key){
        //注册gdal
        gdal.AllRegister();
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");

        SpatialReference ref = new SpatialReference();
        //只有这个投影支持输出geotiff OGC WKT      错误参数：WGS84
        ref.SetWellKnownGeogCS("WGS84");
        String[] defWkt = null;
        //ref.ExportToWkt();// 可配置String数组 defWkt
        //驱动名称
        //String strDriverName = "PNG";
        String strDriverName = "MEM";
        org.gdal.gdal.Driver oDriver = gdal.GetDriverByName(strDriverName);
        if (oDriver == null) {
            System.out.println(strDriverName+ " 驱动不可用！\n");
            return;
        }

        // @param数 (name, xsize, ysize, bands, java.lang.String[] options)
        String[] options = null;// 6个参数
        Dataset dataset = oDriver.Create("msmDS", x.length, y.length, 4);
        dataset.SetProjection(ref.toString());
        // @param double[] argin     地理坐标 米转经纬度单位
        // 0:左上角x坐标 1:东西方向空间分辨率 2:x方向旋转角0° 3:左上角y坐标 4:y方向旋转角0° 5:南北方向空间分辨率
        double [] argin = {LEFT_LON, x[1]-x[0], 0.0,TOP_LAT, 0.0,y[1]-y[0]};
        dataset.SetGeoTransform(argin);
        // @param int xoff(偏移量), int yoff, int xsize, int ysize, int buf_type, byte[] array
        Band band1 = dataset.GetRasterBand(1);// 波段     (色彩)
        Band band2 = dataset.GetRasterBand(2);// 波段     (色彩)
        Band band3 = dataset.GetRasterBand(3);// 波段     (色彩)
        Band band4 = dataset.GetRasterBand(4);// 波段     (色彩)
        //double[][] txtArray = Utils.readTxtToArray(txtArrayFile);
        int[] arr1 = new int[x.length *y.length];
        int[] arr2 = new int[x.length *y.length];
        int[] arr3 = new int[x.length *y.length];
        int[] arr4 = new int[x.length *y.length];
        int c = 0;
        for (int j = 0; j < x.length /*txtArray.length*/; j++) {
            for (int j2 = 0; j2 < y.length/*txtArray[j].length*/; j2++) {
                float v = 0f;
                if(999999!=cressman[j][j2]) {
                    v = (float) cressman[j][j2];

                }else {
                    v = (float) cressman2[j][j2];
                }
                if(ReadGeoInfo.maskArr[j][j2]==1.0) {
                    arr4[c] = 127;
                }else {
                    arr4[c] = 0;
                }
                //空气温度、相对湿度、降水、10分钟平均风速
                int[] floats = null;
                if("空气温度".equals(key)){
                    floats = TiffColor.temperatureColor(v);
                }else if("相对湿度".equals(key)){
                    floats = TiffColor.waterColor(v);
                }else if("降水".equals(key)){
                    floats = TiffColor.rainColor(v);
                }else if("10分钟平均风速".equals(key)){
                    floats = TiffColor.windColor(v);
                }
                if(null != floats) {


                    arr1[c] = floats[0];
                    arr2[c] = floats[1];
                    arr3[c] = floats[2];
                }else {
                    arr4[c] = 0;
                }
                c++;

            }
        }
        // ReadRaster?(int xoff, int yoff, int xsize, int ysize, int buf_type, float[] array)
        // 参数说明： 起始x 起始y x数量(行) y数量(列) 数据类型 数组
        band1.WriteRaster(0, 0, x.length, y.length, arr1);
        band2.WriteRaster(0, 0, x.length, y.length,  arr2);
        band3.WriteRaster(0, 0, x.length, y.length,  arr3);
        band4.WriteRaster(0, 0, x.length, y.length,  arr4);

        //CreateCopy方式创建jpeg格式文件：
        //接上面的过程，先不delete，（即已经完成用create方式先将运算完毕的图像创建为tiff格式）
        String name="PNG";
        // String name="BMP";
        org.gdal.gdal.Driver poDriver = gdal.GetDriverByName(name);
        Dataset datasetJPEG = poDriver.CreateCopy(imagePath, dataset);
        dataset.FlushCache();
        dataset.delete();
        datasetJPEG.delete();
    }

    private static double[] getArr(float start,float end,int num) {
        //起始数，结束数，几等分
        float abs = end-start;
        float v = abs / num;
        double[] d = new double[num];
        for (int i = 0; i < num; i++) {
            d[i]=(start+=v);
        }
        return d;
    }


    /**读取数据 转换没字段：值 的形式*/
    private  List<Map> readTxt()  {
        boolean flag = true;
        String body = "";
        RestTemplate restTemplate = new RestTemplate();
        List<Map> list = null;
        while(flag) {
            try {
                ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
                if (null != forEntity && HttpStatus.HTTP_OK == forEntity.getStatusCode().value() && !StringUtils.isEmpty(forEntity.getBody())) {
                    body = forEntity.getBody();
                    list = JSON.parseArray(body, Map.class);
                    if (list != null && list.size()>0) {
                        flag = false;
                    }
                }
            }catch (Exception e) {
                log.error("获取气象数据失败,将1分钟后重试",e);
            }finally {
                if(flag) {
                    try {
                        Thread.sleep(1000*60);
                    }catch (Exception e) {
                        log.error("休眠一分钟发生错误",e);
                    }

                }
            }
            log.info("获取气象数据失败,将1分钟后重试");
        }

        log.info(" get weather data success");
        //存入数据库
        List<WeatherDataDTO> weatherDataDTOList = new ArrayList<>(list.size());
        for (Map map : list) {
           String rain = (String) map.get("降水");
           String wind = (String) map.get("10分钟平均风速");
           String station = (String) map.get("站名");
           String temp = (String) map.get("空气温度");
           String humi = (String) map.get("相对湿度");
           String time = (String) map.get("时间");
            Date date = DateUtils.stringToDate(time, "yyyyMMddHHmm");
            time = DateUtils.dateToString(date,"yyyyMMddHH");
            time +="00";
           WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
           weatherDataDTO.setTemp(temp);
           weatherDataDTO.setRain(rain);
           weatherDataDTO.setStationName(station);
           weatherDataDTO.setWind(wind);
           weatherDataDTO.setHumidity(humi);
           weatherDataDTO.setTime(time);
           weatherDataDTOList.add(weatherDataDTO);
        }
        weatherDataMapper.insertBatch(weatherDataDTOList);

        List<Map> list1 = new ArrayList<>();
        for (int i1 = 0; i1 < list.size(); i1++) {
            Object obj = list.get(i1).get("站号");
            if(null == obj) {
                log.error("there is no 站号 in " + JSON.toJSONString(list.get(i1)));
               // list.remove(i1);
                continue;
            }
            String  stationID = (String)obj;
            StationInfoDTO stationInfoDTO = stationInfoMap.get(stationID);
            if(null == stationInfoDTO) {
                log.error("there is no stationInfoDTO in " + JSON.toJSONString(list.get(i1)));
                // list.remove(i1);
                continue;
            }
            list.get(i1).put("Lat",stationInfoDTO.getLat());
            list.get(i1).put("Lon",stationInfoDTO.getLon());
            String string = DateUtils.dateToString(new Date(), "yyyyMMddHH");
            list.get(i1).put("时间",string+"00");
            list1.add(list.get(i1));
        }
        return list1;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<StationInfoDTO> stationLists = stationInfoMapper.getStationLists();
        if (null != stationLists) {
            for (StationInfoDTO station : stationLists) {
                stationInfoMap.put(station.getStationID(),station);
            }

        }
        log.info("init stationInfoMap:" + JSON.toJSONString(stationInfoMap));
    }

    public static void main(String[] args) {
        String str = "202004070500";
        System.out.println(str.substring(0, str.length() - 2));
    }

}

