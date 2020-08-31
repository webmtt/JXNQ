package com.htht.cn.jiaxing.interceptor;

import com.alibaba.fastjson.JSON;
import com.htht.cn.jiaxing.constant.Consts;
import com.htht.cn.jiaxing.model.User;
import com.htht.cn.jiaxing.utils.ConstantUtils;
import com.htht.cn.jiaxing.utils.RSAUtils;
import com.htht.cn.jiaxing.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Value("${user.isNeedLogin}")
    String loginFlag;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if(!"Y".equals(loginFlag)) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if(requestURI.contains("/officialAccounts/")) {
            //公众号登录
            User user = (User) request.getSession().getAttribute(ConstantUtils.USER_SESSION_KEY);
            if(null == user) {
                toLogin(response);
                return false;
            }
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                return false;
            }
            try {
                String s = RSAUtils.decryptByPrivateKey(token);
                if(s.equals(user.getPhone_number())) {
                    return true;
                }

            }catch (Exception e) {
                log.error("",e);
            }
            return false;
        }else {
            User user = (User) request.getSession().getAttribute(ConstantUtils.USER_SESSION_KEY);
            //如果session中没有user，表示没登陆
            if (user == null) {
                //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
                //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
                //response.sendRedirect(request.getContextPath()+"/jxdist/index.html");
                toLogin(response);
                return false;
            } else {
                return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
            }
        }

    }

    private void toLogin(HttpServletResponse response) {
        Result result = Result.error(Consts.UN_LOGIN,"登录失效,请先登录");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String string = JSON.toJSONString(result);
        response.setStatus(200);
        try {
            response.getWriter().append(string);
        }catch (Exception e) {
            log.error("",e);
        }
    }
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        // TODO Auto-generated method stub
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {
//        // TODO Auto-generated method stub
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }

}
