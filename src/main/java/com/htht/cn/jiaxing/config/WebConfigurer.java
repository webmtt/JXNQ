package com.htht.cn.jiaxing.config;

import com.htht.cn.jiaxing.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        // addPathPatterns("/**") 拦截所有的请求，
        // excludePathPatterns("/login", "/register") 添加不拦截路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                                     "/user/register",
                                     "/user/login",
                                     "/swagger-ui.html",
                                     "/officialAccounts/userRegister",
                                     "/officialAccounts/userLogin",
                                     "/user/logout");
    }

}

