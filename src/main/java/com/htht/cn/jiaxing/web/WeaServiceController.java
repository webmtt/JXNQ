package com.htht.cn.jiaxing.web;


import com.alibaba.fastjson.JSONObject;
import com.htht.cn.jiaxing.config.WeatherParamConfiguration;
import com.htht.cn.jiaxing.model.WeatherDisasterParameter;
import com.htht.cn.jiaxing.service.WeatherDisasterService;
import com.htht.cn.jiaxing.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.io.file.FileMode.r;

@Slf4j
@RestController
@Api(description = "气象服务接口")
public class WeaServiceController {

    @Autowired
    private WeatherDisasterService weatherDisasterService;

    @Value("${weather.qiujidiwen.pythonAdd}")
    private String qiujidiwenAdd;

    @Value("${weather.fenniejianhuan.pythonAdd}")
    private String feiniejianhuanAdd;

    @Value("${weather.baoyu.pythonAdd}")
    private String baoyuAdd;

    @Value("${weather.honglao.pythonAdd}")
    private String honglaoAdd;

    @Value("${weather.lianyinyu.pythonAdd}")
    private String lianyinyuAdd;

    @Value("${weather.lanyang.pythonAdd}")
    private String lanyangAdd;

    @Value("${weather.read.pythonAdd}")
    private String weatherReadPythonAdd;

    @ApiOperation(value = "气象服务七天天气预报")
    @RequestMapping(value = "/weather/querySevenDaysWeather", method = RequestMethod.GET)
    public Result querySevenDaysWeather() {
        //nc文件路径
        //String site = "/export/GX/data/2020051210.nc";
        String site = NcFileSiteUtil.getSite();
        String pythonAdd = weatherReadPythonAdd;
        System.out.println("====" + site + "=====");
        String ord = "python3";
        String[] arr = {ord, pythonAdd, site};
        String s = JavaForPython.javaForPython(arr);
        JSONObject jsonObject = JSONObject.parseObject(s);
        return Result.ok().put("data", jsonObject);
    }

    @ApiOperation(value = "农业气象灾害预警初始化")
    @RequestMapping(value = "/weather/getStatusByWeathers", method = RequestMethod.GET)
    public Result getStatusByWeathers() {
        Map<String, Boolean> map = new HashMap<>();
        //nc文件路径
        //String site = "/export/GX/data/2020051210.nc";
        String site = NcFileSiteUtil.getSite();
        String ord = "python3";
        String imgOutPath = WeaServiceController.class.getClassLoader().getResource("/").getPath().replaceFirst("file:", "") + "static" + "/out";
        File file = new File(imgOutPath);
        if (!file.exists()) {
            // 按照指定的路径创建文件夹
            boolean b = file.setWritable(true, false);
            boolean mkdirs = file.mkdirs();
        }

        Date date = new Date();
        int monthOfDate = DateUtils.getMonthOfDate(date);

        //String qiujidiwenAdd = "/export/GX/data/qiujidiwen.py";
        if (monthOfDate>=9){
            String a1 = weatherDisasterService.getParams("qiujidiwen");
            JSONObject jsonObject1 = JSONObject.parseObject(a1);
            String qiujidiwenThreshold = jsonObject1.get("qiujidiwenThreshold").toString();
            String qiujidiwenStep = jsonObject1.get("qiujidiwenStep").toString();
            String[] qiujidiwenArr = {ord, qiujidiwenAdd, site, qiujidiwenThreshold, qiujidiwenStep, imgOutPath + "/qiujidiwen.png"};
            String qiujidiwen = JavaForPython.javaForPython(qiujidiwenArr);
            map.put("qiujidiwen", Boolean.parseBoolean(qiujidiwen));
        }else {
            map.put("qiujidiwen", false);
        }

        //String feiniejianhuanAdd = "/export/GX/data/fenniejianhuan.py";
        if (monthOfDate>=8) {
            String a2 = weatherDisasterService.getParams("fenniejianhuan");
            JSONObject jsonObject2 = JSONObject.parseObject(a2);
            String feiniejianhuanThreshold = jsonObject2.get("feiniejianhuanThreshold").toString();
            String feiniejianhuanStep = jsonObject2.get("feiniejianhuanStep").toString();
            String[] feiniejianhuanArr = {ord, feiniejianhuanAdd, site, feiniejianhuanThreshold, feiniejianhuanStep, imgOutPath + "/fenniejianhuan.png"};
            String feiniejianhuan = JavaForPython.javaForPython(feiniejianhuanArr);
            map.put("fenniejianhuan", Boolean.parseBoolean(feiniejianhuan));
        }else {
            map.put("fenniejianhuan", false);
        }

        //String baoyuAdd = "/export/GX/data/baoyu.py";
        String a3 = weatherDisasterService.getParams("baoyu");
        JSONObject jsonObject3 = JSONObject.parseObject(a3);
        String baoyuThreshold = jsonObject3.get("baoyuThreshold").toString();
        String[] baoyuArr = {ord, baoyuAdd, site, baoyuThreshold, imgOutPath + "/baoyu.png"};
        String baoyu = JavaForPython.javaForPython(baoyuArr);
        map.put("baoyu", Boolean.parseBoolean(baoyu));

        //String honglaoAdd = "/export/GX/data/honglao.py";
        String a4 = weatherDisasterService.getParams("honglao");
        JSONObject jsonObject4 = JSONObject.parseObject(a4);
        String honglaoThreshold1 = jsonObject4.get("honglaoThreshold1").toString();
        String honglaoThreshold2 = jsonObject4.get("honglaoThreshold2").toString();
        String honglaoThreshold3 = jsonObject4.get("honglaoThreshold3").toString();
        String[] honglaoArr = {ord, honglaoAdd, site, honglaoThreshold1, honglaoThreshold2, honglaoThreshold3, imgOutPath + "/honglao.png"};
        String honglao = JavaForPython.javaForPython(honglaoArr);
        map.put("honglao", Boolean.parseBoolean(honglao));

        //String lianyinyuAdd = "/export/GX/data/lianyinyu.py";
        String a5 = weatherDisasterService.getParams("lianyinyu");
        JSONObject jsonObject5 = JSONObject.parseObject(a5);
        String lianyinyuThreshold = jsonObject5.get("lianyinyuThreshold").toString();
        String lianyinyuStep = jsonObject5.get("lianyinyuStep").toString();
        String[] lianyinyuArr = {ord, lianyinyuAdd, site, lianyinyuThreshold, lianyinyuStep, imgOutPath + "/lianyinyu.png"};
        String lianyinyu = JavaForPython.javaForPython(lianyinyuArr);
        map.put("lianyinyu", Boolean.parseBoolean(lianyinyu));

        //String lanyangAdd = "/export/GX/data/lanyang.py";
        String a6 = weatherDisasterService.getParams("lanyang");
        JSONObject jsonObject6 = JSONObject.parseObject(a6);
        String lanyangPrThreshold = jsonObject6.get("lanyangPrThreshold").toString();
        String lanyangTeThreshold = jsonObject6.get("lanyangTeThreshold").toString();
        String lanyangStep = jsonObject6.get("lanyangStep").toString();
        String[] lanyangArr = {ord, lanyangAdd, site, lanyangTeThreshold, lanyangPrThreshold, lanyangStep, imgOutPath + "/lanyang.png"};
        String lanyang = JavaForPython.javaForPython(lanyangArr);
        map.put("lanyang", Boolean.parseBoolean(lanyang));

        return Result.ok().put("data", map);

    }

    @ApiOperation(value = "获取气象灾害预警初始化参数")
    @RequestMapping(value = "/weather/getParamsByWeather", method = RequestMethod.GET)
    public Result getParamsByWeather(@RequestParam(value = "param") String param) {
        //Map<String, String> map = weatherParamConfiguration.getParamMaps().get(param);
        String url = "/out/";
        //map.put("url", url + param + ".png");
        String a = weatherDisasterService.getParams(param);
        JSONObject jsonObject = JSONObject.parseObject(a);
        jsonObject.put("url", url + param + ".png");
        return Result.ok().put("data", jsonObject);
    }

    @ApiOperation(value = "农业气象灾害预警参数变化调用算法")
    @RequestMapping(value = "/weather/getStatusByWeatherParams", method = RequestMethod.GET)
    public Result getStatusByWeatherParams(@RequestParam(value = "type") String type, @RequestParam(value = "param") String param) {
        //nc文件路径
        //String site = "/export/GX/data/2020051210.nc";
        String site = NcFileSiteUtil.getSite();
        String ord = "python3";
        String imgOutPath = WeaServiceController.class.getClassLoader().getResource("/").getPath().replaceFirst("file:", "") + "static" + "/out";
        File file = new File(imgOutPath);
        if (!file.exists()) {
            // 按照指定的路径创建文件夹
            file.setWritable(true, false);
            file.mkdirs();
        }
        if (("qiujidiwen").equals(type) && param != null) {
            //String qiujidiwenAdd = "/export/GX/data/qiujidiwen.py";
            JSONObject jsonObject = JSONObject.parseObject(param);
            String qiujidiwenThreshold = jsonObject.get("qiujidiwenThreshold").toString();
            String qiujidiwenStep = jsonObject.get("qiujidiwenStep").toString();
            String[] qiujidiwenArr = {ord, qiujidiwenAdd, site, qiujidiwenThreshold, qiujidiwenStep, imgOutPath + "/qiujidiwen.png"};
            String qiujidiwen = JavaForPython.javaForPython(qiujidiwenArr);

            WeatherDisasterParameter parameter = new WeatherDisasterParameter();
            parameter.setCreateTime(new Date());
            parameter.setdType("qiujidiwen");
            parameter.setdParameter(param);
            int a = weatherDisasterService.insert(parameter);
            if (qiujidiwen != null && qiujidiwen.length() != 0 && a > 0) {
                return Result.ok();
            }
        }

        if (("fenniejianhuan").equals(type) && param != null) {
            //String feiniejianhuanAdd = "/export/GX/data/fenniejianhuan.py";
            JSONObject jsonObject = JSONObject.parseObject(param);
            String feiniejianhuanThreshold = jsonObject.get("feiniejianhuanThreshold").toString();
            String feiniejianhuanStep = jsonObject.get("feiniejianhuanStep").toString();
            String[] feiniejianhuanArr = {ord, feiniejianhuanAdd, site, feiniejianhuanThreshold, feiniejianhuanStep, imgOutPath + "/fenniejianhuan.png"};
            String feiniejianhuan = JavaForPython.javaForPython(feiniejianhuanArr);

            WeatherDisasterParameter parameter = new WeatherDisasterParameter();
            parameter.setCreateTime(new Date());
            parameter.setdType("feiniejianhuan");
            parameter.setdParameter(param);
            int a = weatherDisasterService.insert(parameter);
            if (feiniejianhuan != null && feiniejianhuan.length() != 0 && a > 0) {
                return Result.ok();
            }
        }

        if (("baoyu").equals(type) && param != null) {
            //String baoyuAdd = "/export/GX/data/baoyu.py";
            JSONObject jsonObject = JSONObject.parseObject(param);
            String baoyuThreshold = jsonObject.get("baoyuThreshold").toString();
            String[] baoyuArr = {ord, baoyuAdd, site, baoyuThreshold, imgOutPath + "/baoyu.png"};
            String baoyu = JavaForPython.javaForPython(baoyuArr);

            WeatherDisasterParameter parameter = new WeatherDisasterParameter();
            parameter.setCreateTime(new Date());
            parameter.setdType("baoyu");
            parameter.setdParameter(param);
            int a = weatherDisasterService.insert(parameter);
            if (baoyu != null && baoyu.length() != 0 && a > 0) {
                return Result.ok();
            }
        }

        if (("honglao").equals(type) && param != null) {
            //String honglaoAdd = "/export/GX/data/honglao.py";
            JSONObject jsonObject = JSONObject.parseObject(param);
            String honglaoThreshold1 = jsonObject.get("honglaoThreshold1").toString();
            String honglaoThreshold2 = jsonObject.get("honglaoThreshold2").toString();
            String honglaoThreshold3 = jsonObject.get("honglaoThreshold3").toString();
            String[] honglaoArr = {ord, honglaoAdd, site, honglaoThreshold1, honglaoThreshold2, honglaoThreshold3, imgOutPath + "/honglao.png"};
            String honglao = JavaForPython.javaForPython(honglaoArr);

            WeatherDisasterParameter parameter = new WeatherDisasterParameter();
            parameter.setCreateTime(new Date());
            parameter.setdType("honglao");
            parameter.setdParameter(param);
            int a = weatherDisasterService.insert(parameter);
            if (honglao != null && honglao.length() != 0 && a > 0) {
                return Result.ok();
            }
        }

        if (("lianyinyu").equals(type) && param != null) {
            //String lianyinyuAdd = "/export/GX/data/lianyinyu.py";
            JSONObject jsonObject = JSONObject.parseObject(param);
            String lianyinyuThreshold = jsonObject.get("lianyinyuThreshold").toString();
            String lianyinyuStep = jsonObject.get("lianyinyuStep").toString();
            String[] lianyinyuArr = {ord, lianyinyuAdd, site, lianyinyuThreshold, lianyinyuStep, imgOutPath + "/lianyinyu.png"};
            String lianyinyu = JavaForPython.javaForPython(lianyinyuArr);

            WeatherDisasterParameter parameter = new WeatherDisasterParameter();
            parameter.setCreateTime(new Date());
            parameter.setdType("lianyinyu");
            parameter.setdParameter(param);
            int a = weatherDisasterService.insert(parameter);
            if (lianyinyu != null && lianyinyu.length() != 0 && a > 0) {
                return Result.ok();
            }
        }

        if (("lanyang").equals(type) && param != null) {
            //String lanyangAdd = "/export/GX/data/lanyang.py";
            JSONObject jsonObject = JSONObject.parseObject(param);
            String lanyangPrThreshold = jsonObject.get("lanyangPrThreshold").toString();
            String lanyangTeThreshold = jsonObject.get("lanyangTeThreshold").toString();
            String lanyangStep = jsonObject.get("lanyangStep").toString();
            String[] lanyangArr = {ord, lanyangAdd, site, lanyangTeThreshold, lanyangPrThreshold, lanyangStep, imgOutPath + "/lanyang.png"};
            String lanyang = JavaForPython.javaForPython(lanyangArr);

            WeatherDisasterParameter parameter = new WeatherDisasterParameter();
            parameter.setCreateTime(new Date());
            parameter.setdType("lanyang");
            parameter.setdParameter(param);
            int a = weatherDisasterService.insert(parameter);
            if (lanyang != null && lanyang.length() != 0 && a > 0) {
                return Result.ok();
            }
        }
        return Result.error();

    }


}
