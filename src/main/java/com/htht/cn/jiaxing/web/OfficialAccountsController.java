package com.htht.cn.jiaxing.web;

import com.htht.cn.jiaxing.model.LandBlockModel;
import com.htht.cn.jiaxing.model.LandsQueryVO;
import com.htht.cn.jiaxing.model.OfficialAccountUserModel;
import com.htht.cn.jiaxing.model.User;
import com.htht.cn.jiaxing.model.dto.OfficialAccountUserDTO;
import com.htht.cn.jiaxing.service.OfficialAccountsService;
import com.htht.cn.jiaxing.utils.ConstantUtils;
import com.htht.cn.jiaxing.utils.RSAUtils;
import com.htht.cn.jiaxing.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *zw
 * 20200317
 * 微信公众号
 *
 * */
@Api(description = "公众号")
@RestController
@Slf4j
public class OfficialAccountsController extends BaseController{

	@Autowired
	private OfficialAccountsService officialAccountsService;

	@Value("${user.login-living}")
    private String livingTime;


	@ApiOperation(value="绑定地块")
	@PostMapping("/officialAccounts/landBind")
	public Result landBind(@RequestBody  LandBlockModel landBlockModel, HttpServletRequest request){
		User user = (User) request.getSession().getAttribute(ConstantUtils.USER_SESSION_KEY);
		if(null == user) {
			return Result.error("用户登录超时");
		}
		String s = officialAccountsService.landBind(landBlockModel,user);
		if(StringUtils.isEmpty(s)) {
			return Result.ok();
		}else {
			return Result.error(s);
		}
	}

	@ApiOperation(value="绑定地块 编辑")
	@PostMapping("/officialAccounts/landEdit")
	public Result landEdit(@RequestBody LandBlockModel landBindModel){
		String s = officialAccountsService.landEdit(landBindModel);
		if(StringUtils.isEmpty(s)) {
			return Result.ok();
		}else {
			return Result.error(s);
		}
	}

	@ApiOperation(value="绑定地块 删除")
	@GetMapping("/officialAccounts/landDel")
	public Result landDel(@RequestParam String landId){
		String s = officialAccountsService.landDel(landId);
		if(StringUtils.isEmpty(s)) {
			return Result.ok();
		}else {
			return Result.error(s);
		}
	}

	@ApiOperation(value = "查询绑定地块信息")
	@GetMapping("/officialAccounts/queryLand")
	public Result queryLand(@RequestParam Long userid,HttpServletRequest request){
		if(userid == null) {
			return Result.error("用户id不存在");
		}
		User user = (User) request.getSession().getAttribute(ConstantUtils.USER_SESSION_KEY);
//        if(null ==user) {
//            return Result.error("用户登录失效");
//        }
//		if(user.getUser_id() != userid) {
//			return Result.error("只允许查询当前账号的信息");
//		}
		try {
			LandsQueryVO landsQueryVO = officialAccountsService.queryLands(userid);
			return Result.ok(landsQueryVO);
		}catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	//注册
	@PostMapping("/officialAccounts/userRegister")
	public Result userAdd(@RequestBody OfficialAccountUserModel user, HttpServletRequest request){
		log.info("start register:"+user);
		String register = officialAccountsService.register(user);
		if(StringUtils.isEmpty(register)) {
			return success();
		}else {
			return failue(register);
		}
	}

	//登录
	@PostMapping("/officialAccounts/userLogin")
	public Result userLogin(@RequestBody OfficialAccountUserModel user, HttpServletRequest request) throws Exception {
		log.info("start login:"+user);
		Object msg = officialAccountsService.login(user);
		if(msg instanceof String) {
			return failue((String)msg);
		}else {
			OfficialAccountUserDTO dto = (OfficialAccountUserDTO) msg;
			User user1 = new User();
				user1.setPhone_number(dto.getMobile());
				user1.setUser_name(dto.getUserName());
				user1.setUser_id(dto.getId());
				request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY, user1);
				request.getSession().setMaxInactiveInterval(Integer.valueOf(livingTime));
				String token = RSAUtils.encryptByPublicKey(user1.getPhone_number());

				log.info("login success:" + user);
				Map map = new HashMap();
				map.put("token", token);
				map.put("userId", dto.getId());
				return success(map);

		}
	}

	//查询用户信息
	@GetMapping("/officialAccounts/userInfo")
	public Result userInfo(@RequestParam Long userId) {
		OfficialAccountUserDTO officialAccountUserDTO = officialAccountsService.findUserById(userId);
		return success(officialAccountUserDTO);
	}

}
