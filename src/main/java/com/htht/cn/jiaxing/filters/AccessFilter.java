package com.htht.cn.jiaxing.filters;

import com.alibaba.fastjson.JSON;
import com.htht.cn.jiaxing.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.htht.cn.jiaxing.constant.Consts.ACCESS_DENY;

@Slf4j
@Component
@Order(value = 1)
public class AccessFilter extends OncePerRequestFilter {

	public static final String TOKEN_KEY = "token";

	//两个map 防止系统被刷
	private static Map<String,Integer> first = new ConcurrentHashMap<>(10240);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String remoteUser = getIpAddr(request);
		Integer integer = first.get(remoteUser);
		if(null == integer) {
			integer = 1;
			first.put(remoteUser,integer);
			filterChain.doFilter(request, response);
		}else {
			//默认半个小时内访问600次
			if (integer < 600) {
				integer = integer + 1;
				first.put(remoteUser, integer);
				filterChain.doFilter(request, response);
			} else {
				toStop(response);
			}
		}
	}

	@Scheduled(cron = "0 0/30 * * * ?")
	public void initMap(){
		first.clear();
	}



	private void toStop(HttpServletResponse response) {
		Result result = Result.error(ACCESS_DENY,"访问次数超出限制,请等30分钟再试");
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

	/**
	 * 获取访问者IP
	 *
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 *
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 *
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

}
