# -*- coding: utf-8 -*-
# -*- coding: utf-8 -*-
"""
Created on Thu Mar 12 09:47:04 2020

@author: LX
"""
import netCDF4 as nc
import numpy as np
import cv2
import pyexiv2
from pyexiv2 import Image
import PIL.Image as IImage


def pre_day(pr_data, time_data, time_start):  # 获取20点降水数据
    pre_day = []
    h = int(time_start[8:10])
    for i in range(len(time_data)):
        if (time_data[i] + h) % 24 == 20:
            pre_day.append(pr_data[i])
    return pre_day


def Ww12_day_day(Ww12_data, time_data, time_start):  # 获取20点天气数据
    Ww12_day = []
    h = int(time_start[8:10])
    for i in range(len(time_data)):
        if (time_data[i] + h) % 24 == 20:
            Ww12_day.append(Ww12_data[i])
    return Ww12_day


def transparent_back(img):
    img = img.convert("RGBA")
    L, H = img.size
    color_0 = (0, 0, 0, 255)
    for h in range(H):
        for l in range(L):
            dot = (l, h)
            color_1 = img.getpixel(dot)
            if color_1 == color_0:
                color_1 = color_1[:-1] + (0,)
                img.putpixel(dot, (0, 0, 0, 0))
    return img


def several_rain(
    pr_data, Ww12_data, time_data, time_start, pr_Threshold, step, weather, outputname
):  # 计算烂秧指数
    pre_data_day = pre_day(pr_data, time_data, time_start)
    Ww12_day = Ww12_day_day(Ww12_data, time_data, time_start)
    H, W = pre_data_day[0].shape
    new_im = np.zeros((H, W))
    for i in range(H):
        for j in range(W):
            #            print('percent   '+str(round((i*W+j)/(H*W)*100,4))+'%')
            for k in range(15 - step):
                #                print('percent   '+str(round((i*W+j)/(H*W)*100,4))+'%')
                pre_4 = []
                Ww12_4 = []
                for l in range(step):
                    pre_4.append(pre_data_day[k + l][i, j])
                    Ww12_4.append(Ww12_day[k + l][i, j])
                #                print(len([pre for pre in pre_4 if pre > pr_Threshold]),[pre for pre in pre_4 if pre > pr_Threshold])
                #                print(len([Ww for Ww in Ww12_4 if Ww in weather]),[Ww for Ww in Ww12_4 if Ww in weather])
                #                print(len([pre for pre in pre_4 if pre > pr_Threshold])>=step and len([Ww for Ww in Ww12_4 if Ww in weather])>=step)
                if (
                    len([pre for pre in pre_4 if pre > pr_Threshold]) >= step
                    and len([Ww for Ww in Ww12_4 if Ww in weather]) >= step
                ):
                    new_im[i, j] = 255
    fnjh_out = new_im
    fnjh_out = fnjh_out.astype(np.float32)
    fnjh_out = cv2.resize(fnjh_out, (440, 340), interpolation=cv2.INTER_NEAREST)
    cv2.imwrite(outputname, fnjh_out)
    print("*******Calculate complete！*******")


def coordinate(lat_data, lon_data):  # 获取数据地理范围
    lef_up = (lon_data[0], lat_data[-1])
    rig_do = (lon_data[-1], lat_data[0])
    coo = [lef_up, rig_do]
    return coo


def writ_gps(image_path, coo):
    # 转换并写入图像地理范围，存储于Xmp.dc字段下(x1、y1)(x2,y2)下
    # {'Xmp.dc.x1': 'E120.25',
    # 'Xmp.dc.y1': 'N31.05000000000001',
    # 'Xmp.dc.x2': 'E121.29999999999994',
    # 'Xmp.dc.y2': 'N30.25'}

    if coo[0][0] > 0:
        long_location_lef = "E"
    else:
        long_location_lef = "W"
    if coo[0][1] > 0:
        lati_location_lef = "N"
    else:
        lati_location_lef = "S"
    if coo[1][0] > 0:
        long_location_rig = "E"
    else:
        long_location_rig = "W"
    if coo[1][1] > 0:
        lati_location_rig = "N"
    else:
        lati_location_rig = "S"

    longituade_lef = abs(coo[0][0])
    latituade_lef = abs(coo[0][1])
    longituade_rig = abs(coo[1][0])
    latituade_rig = abs(coo[1][1])

    point1_x1 = str(long_location_lef) + str(longituade_lef) + "°"
    point1_y1 = str(lati_location_lef) + str(latituade_lef) + "°"
    point2_x1 = str(long_location_rig) + str(longituade_rig) + "°"
    point2_y2 = str(lati_location_rig) + str(latituade_rig) + "°"

    img = Image(image_path)
    img.modify_xmp({"Xmp.dc.x1": point1_x1})
    img.modify_xmp({"Xmp.dc.y1": point1_y1})
    img.modify_xmp({"Xmp.dc.x2": point2_x1})
    img.modify_xmp({"Xmp.dc.y2": point2_y2})
    img.close()


def disaster(img):  # 判断是否出现灾情
    if np.max(img) > 0:
        print(True)
    else:
        print(False)


if __name__ == "__main__":
    #    filename = sys.argv[1]
    #    pr_Threshold = sys.argv[2]
    #    te_Threshold = sys.argv[3]
    #    outpath = sys.argv[4]

    filename = r"C:\Users\LX\Desktop\zngxjx\nc\2019110110.nc"  # .nc文件名
    outputname = r"C:\Users\LX\Desktop\zngxjx\output\4.png"
    pr_Threshold = 0.1  # 输入阈值
    step = 4
    weather = {
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        19,
        21,
        22,
        23,
        24,
        25,
        26,
        27,
        28,
    }

    f = nc.Dataset(filename)  # 读取.nc文件，传入f中。此时f包含了该.nc文件的全部信息

    pr = "Pr24"
    pr_data = f[pr][:]  # 获取变量的数据
    pr_data = np.array(pr_data)  # 转化为np.array数组

    Ww12 = "Ww12"
    Ww12_data = f[Ww12][:]
    Ww12_data = np.array(Ww12_data)  # 转化为np.array数组

    tim = f.variables["time"]
    units = tim.units
    units_time = units.split(" ")
    time_start = units_time[-1]

    time = "time"
    time_data = f[time][:]
    time_data = np.array(time_data)

    lat = "lat"
    lat_data = f[lat][:]
    lat_data = np.array(lat_data)

    lon = "lon"
    lon_data = f[lon][:]
    lon_data = np.array(lon_data)

    several_rain(
        pr_data,
        Ww12_data,
        time_data,
        time_start,
        pr_Threshold,
        step,
        weather,
        outputname,
    )
    coo = coordinate(lat_data, lon_data)
    img1 = IImage.open(outputname)
    img1 = transparent_back(img1)
    img1.save(outputname)
    writ_gps(outputname, coo)

    # 判断灾情
    disaster(img1)

#    img = Image(outputname)
#    img.read_xmp()
#    img.close()
