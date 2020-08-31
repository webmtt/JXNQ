package com.htht.cn.jiaxing.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * 
 * @author lixulei
 * @date 2020/2/21 下午3:55:19
 */
public class Result extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Result() {
		put("returnCode", 0);
	}
	
	public static Result error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static Result error(String returnMessage) {
		return error(500, returnMessage);
	}
	
	public static Result error(int returnCode, String returnMessage) {
		Result r = new Result();
		r.put("returnCode", returnCode);
		r.put("returnMessage", returnMessage);
		return r;
	}

	public static Result ok(String returnMessage) {
		Result r = new Result();
		r.put("returnMessage", returnMessage);
		return r;
	}
	
	public static Result ok(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}

	public static Result ok(Object obj) {
		Result r = new Result();
		r.put("returnMessage", "success");
		r.put("returnCode", 0);
		r.put("data",obj);
		return r;
	}
	
	public static Result ok() {
		return new Result();
	}

	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
