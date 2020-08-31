package com.htht.cn.jiaxing.utils;

public class TiffColor {
	//温度色带
	//
	public static int[] temperatureColor(float num) {
		int[] res = new int[3];
		if (num < -10) {
			return null;
		}else if (num < -0) {
			res[0] = 0;
			res[1] = 0;
			res[2] = 255;
		}else if (num < 5) {
			res[0] = 103;
			res[1] = 247;
			res[2] = 0;
		}else if (num < 10) {
			res[0] = 128;
			res[1] = 247;
			res[2] = 0;
		}else if (num < 12) {
			res[0] = 157;
			res[1] = 247;
			res[2] = 0;
		}else if (num < 14) {
			res[0] = 186;
			res[1] = 247;
			res[2] = 0;
		}else if (num < 16) {
			res[0] = 210;
			res[1] = 247;
			res[2] = 0;
		}else if (num < 18) {
			res[0] = 233;
			res[1] = 245;
			res[2] = 0;
		}else if (num < 20) {
			res[0] = 247;
			res[1] = 231;
			res[2] = 0;
		}else if (num < 22) {
			res[0] = 250;
			res[1] = 204;
			res[2] = 0;
		}else if (num < 24) {
			res[0] = 252;
			res[1] = 177;
			res[2] = 0;
		}else if (num < 26) {
			res[0] = 252;
			res[1] = 147;
			res[2] = 0;
		}else if (num < 28) {
			res[0] = 252;
			res[1] = 118;
			res[2] = 0;
		}else if (num < 30) {
			res[0] = 250;
			res[1] = 92;
			res[2] = 0;
		}else if (num < 35) {
			res[0] = 247;
			res[1] = 58;
			res[2] = 0;
		}else if (num < 45) {
			res[0] = 245;
			res[1] = 0;
			res[2] = 0;
		}else {

			return null;
		}
		return res;
	}

	//降雨色带
	public static int[] rainColor(float num) {
		int[] res = new int[3];
		if (num > 250) {
			res[0] = 10;
			res[1] = 134;
			res[2] = 229;
		}else if (num >= 100) {
			res[0] = 52;
			res[1] = 196;
			res[2] = 244;
		}else if (num >= 50) {
			res[0] = 88;
			res[1] = 238;
			res[2] = 243;
		}else if (num >= 25) {
			res[0] = 149;
			res[1] = 248;
			res[2] = 205;
		}else if (num >= 10) {
			res[0] = 206;
			res[1] = 252;
			res[2] = 209;
		}else if (num > 0) {
			res[0] = 250;
			res[1] = 254;
			res[2] = 205;
		}else {
			return null;
		}
		return res;

	}
	//风速色带
	public static int[] windColor(float num) {
		int[] res = new int[3];
		if(num>36.9) {
			return null;
		}else if (num >= 32.7) {
			res[0] = 0;
			res[1] = 0;
			res[2] = 255;
		}else if (num >= 28.5) {
			res[0] = 126;
			res[1] = 0;
			res[2] = 230;
		}else if (num >= 24.5) {
			res[0] = 183;
			res[1] = 0;
			res[2] = 196;
		}else if (num >= 20.8) {
			res[0] = 230;
			res[1] = 0;
			res[2] = 164;
		}else if (num >= 17.2) {
			res[0] = 255;
			res[1] = 0;
			res[2] = 136;
		}else if (num >= 13.9) {
			res[0] = 255;
			res[1] = 0;
			res[2] = 106;
		}else if (num >= 10.8) {
			res[0] = 255;
			res[1] = 0;
			res[2] = 81;
		}else if (num >= 8) {
			res[0] = 255;
			res[1] = 0;
			res[2] = 60;
		}else if (num >= 5.5) {
			res[0] = 255;
			res[1] = 66;
			res[2] = 41;
		}else if (num >= 3.4) {
			res[0] = 255;
			res[1] = 106;
			res[2] = 20;
		}else if (num >= 1.6) {
			res[0] = 255;
			res[1] = 140;
			res[2] = 0;
		}else if (num >= 0.3) {
			res[0] = 255;
			res[1] = 170;
			res[2] = 0;
		}else if (num >= 0) {
			res[0] = 255;
			res[1] = 200;
			res[2] = 0;
		}else {
			return null;
		}
		return res;
	}
	//湿度色带
	public static int[] waterColor(float num) {
		int[] res = new int[3];
		if(num>100) {
			return null;
		}else if (num > 90) {
			res[0] = 255;
			res[1] = 0;
			res[2] = 0;
		}else if (num >= 80) {
			res[0] = 255;
			res[1] = 85;
			res[2] = 0;
		}else if (num >= 70) {
			res[0] = 255;
			res[1] = 140;
			res[2] = 0;
		}else if (num >= 65) {
			res[0] = 255;
			res[1] = 191;
			res[2] = 0;
		}else if (num >= 60) {
			res[0] = 255;
			res[1] = 238;
			res[2] = 0;
		}else if (num >= 55) {
			res[0] = 236;
			res[1] = 255;
			res[2] = 64;
		}else if (num >= 50) {
			res[0] = 201;
			res[1] = 255;
			res[2] = 120;
		}else if (num >= 45) {
			res[0] = 158;
			res[1] = 255;
			res[2] = 169;
		}else if (num >= 40) {
			res[0] = 97;
			res[1] = 255;
			res[2] = 221;
		}else if (num >= 35) {
			res[0] = 31;
			res[1] = 236;
			res[2] = 255;
		}else if (num >= 30) {
			res[0] = 56;
			res[1] = 149;
			res[2] = 255;
		}else if (num >= 20) {
			res[0] = 59;
			res[1] = 124;
			res[2] = 255;
		}else if (num >= 10) {
			res[0] = 46;
			res[1] = 70;
			res[2] = 255;
		}else if (num >= 0) {
			res[0] = 0;
			res[1] = 0;
			res[2] = 255;
		}else  {
			return null;
		}
		return res;
	}

}
