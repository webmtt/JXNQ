package com.htht.cn.jiaxing.web;


import com.htht.cn.jiaxing.model.ReturnObject;
import com.htht.cn.jiaxing.utils.Result;

import static com.htht.cn.jiaxing.constant.Consts.FAIL_CODE;
import static com.htht.cn.jiaxing.constant.Consts.SUCCESS_CODE;

public abstract class BaseController {

    protected Result success() {

        return  Result.ok();
    }

    protected Result success(String msg) {
        return Result.ok(msg);
    }

    protected Result success(Object data) {
        return Result.ok(data);
    }

    protected Result failue() {
        return Result.error();
    }

    protected Result failue(String msg) {
        return Result.error(msg);
    }

    protected Result failue(int code,String msg) {
        return Result.error(code ,msg);
    }
}
