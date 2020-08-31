package com.htht.cn.jiaxing.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaForPython {

    public static String javaForPython(String args[]){
        String result = "";

        try {
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor();
            InputStreamReader ir = new InputStreamReader(process.getInputStream(),"gbk");
            LineNumberReader input = new LineNumberReader(ir);
            result = input.readLine();
            input.close();
            ir.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String javaDealPython(String args[]){
        StringBuilder builder = new StringBuilder();
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(args);
            InputStream inputStream =exec.getInputStream();
            BufferedReader reader = null;

            reader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println(builder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        //String site = "F:\\test\\2019120516.nc";
        //String [] aa= new String[]{"C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37\\python","F:/read_weather.py"};
       //String result= JavaForPython.javaForPython(aa);
        //System.out.println(result);
        //String newStr = new String(result.getBytes("iso8859-1"), "utf-8");
        /*String a = "Max_temperature_week: [13.361328125, 10.9912109375, 10.1826171875, 11.67578125, 15.0771484375, 17.0810546875, 15.2470703125] /Min_temperature_week: [3.9228515625, 4.2783203125, 0.3857421875, 2.099609375, 4.1396484375, 6.666015625, 7.7099609375] /Weather_week: [1 1 0 0 1 1 1]";
        String[] split = a.split("/");
        System.out.println(split[0]);
        String[] split1 = split[0].split(":");
        Map map = new HashMap();
        map.put(split1[0],split1[1]);
        System.out.println(map);
        System.out.println(split[1]);
        System.out.println(split[2]);*/
        //String a = "{\n" +"\"feiniejianhuanThreshold\": \"30\",\n" +"\"feiniejianhuanStep\": \"10\"}";
        //JSONObject jsonObject = JSONObject.parseObject(a);
        //jsonObject.put("feiniejianhuanThreshold","50");
        //System.out.println(jsonObject.get("feiniejianhuanThreshold"));
        //Map map = new HashMap();
        //map.put("aaaa","bbbbbb");
        //map.put("cccc","dddddd");
        //map.putIfAbsent("cccc","ddddddddddd");
        //System.out.println(JSONObject.toJSONString(map));
        //System.out.println(map);
    }

}
