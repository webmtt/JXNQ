package com.htht.cn.jiaxing.web;



import com.htht.cn.jiaxing.model.User;
import com.htht.cn.jiaxing.service.UserService;
import com.htht.cn.jiaxing.utils.ConstantUtils;
import com.htht.cn.jiaxing.utils.EncryptUtil;
import com.htht.cn.jiaxing.utils.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class HomeController {

    @Autowired
    private UserService userService;


    @Value("${user.login-living}")
    private String liveTime;

    /**
     * 增删改查
     * @return
     */
    @GetMapping(value = "/findall")
    public Result findAll(@RequestParam(value = "page",defaultValue = "1")int page,
                          @RequestParam(value = "size",defaultValue = "10")int size) {
       Object list = userService.findAll(page,size);
        if (list!=null){
            return Result.ok().put("data",list);
        }
        return Result.error();
    }


    @GetMapping(value = "/findbyid")//批量查询
    public Result findById(@RequestParam(value = "user_id") List<Long> list) {
        List<User> aa = userService.findById(list);
        if (aa!=null){
            return Result.ok().put("data",aa);
        }
        return Result.error();
    }


    @PostMapping(value = "/adduser")
    public Result addAll(User user,String user_name) {
        Integer count1 =userService.findName(user_name);
        if(count1==0) {
            String s = user.getUser_password();//取出user的密码
            String user_password = EncryptUtil.sha1(s);//对密码进行sha1加密
            user.setUser_password(user_password);
            boolean count2 = userService.addUser(user);
            if (count2) {
                return Result.ok().put("msg", "添加成功");
            }
            return Result.error();
        }
        return Result.ok().put("msg","用户名重复");
        }



    @PostMapping(value = "/deletebyid")//删除用户(批量)
    public Result deleteById(@RequestParam(value = "user_id") List<Long> list) {
        boolean count = userService.deleteUser(list);
        if(count) {
            return Result.ok().put("msg", "删除成功");
        }
        return Result.error();
    }


    @PostMapping("/updateuser")//修改基本信息
    public Result updateUser(User user){
        boolean count = userService.setUser(user);
        if(count) {
            return Result.ok().put("msg", "修改成功");
        }
        return Result.error();
    }


    @PostMapping("/changepwd")//修改密码
    public Result changepwd(String user_id, String user_password){
        //通过前端获取session保存的密码判断原密码输入是否正确，然后再确认新密码是否一样最后把新密码传值到后台
        String password= EncryptUtil.sha1(user_password);//加密密码
        boolean count = userService.setPwd(user_id, password);
        if (count){
            return Result.ok().put("msg","修改成功");
        }
        return Result.error();
    }



    /**
     * 用户登录登出与注册
     * @return
     */

    @PostMapping("/register")//注册
    public Result register(User user,String user_name){
        Integer count =userService.findName(user_name);
        if (count==0){
            String s = user.getUser_password();//取出user的密码
            String user_password = EncryptUtil.sha1(s);//对密码进行sha1加密
            user.setUser_password(user_password);
            Integer aa = userService.userRegister(user);
            if (aa>0){
                return Result.ok().put("msg","注册成功");
            }
            return Result.error();
            }
        return Result.error().put("msg","用户名重复");
    }

    /*该接口太繁琐，不使用
    @GetMapping("/login")
    public Result login(String user_name,String user_password,HttpServletRequest request){
        String encryptedPwd = EncryptUtil.sha1(user_password);
        String userPwd = userService.find1ByName(user_name);
<<<<<<< HEAD
       Integer userType =userService.find2ByName(user_name);
        User user = userService.userLogin(user_name,user_password);
        if(encryptedPwd.equals(userPwd) && userType==0 && user!=null){
            request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,user);
            return Result.ok().put("Super manager",user);
        }else if (encryptedPwd.equals(userPwd) && userType==1&& user!=null){
            request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,user);
            return Result.ok().put("Ordinary user",user);
=======
        Integer userType =userService.find2ByName(user_name);
        if(encryptedPwd==userPwd && userType==0){
            List<User> list = userService.userLogin(user_name,user_password);
            request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,list);
            return Result.ok().put("Super manager",list);
        }else if (encryptedPwd==userPwd && userType==1){
            List<User> list = userService.userLogin(user_name,user_password);
            request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,list);
            return Result.ok().put("Ordinary user",list);
>>>>>>> ef154a1b061e4e11fe54337bbe898ba4f8af9315
        }
        return Result.error();
    }*/

    @GetMapping("/login")
    public Result uLogin(String user_name,String user_password,HttpServletRequest request){
        String encryptedPwd = EncryptUtil.sha1(user_password);
        User user = userService.uLogin(user_name,encryptedPwd);
        if(user!= null){
            request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,user);
            //设置失效时间10分钟
            request.getSession().setMaxInactiveInterval(Integer.valueOf(liveTime));
            Object o = request.getSession().getAttribute(ConstantUtils.USER_SESSION_KEY);
            log.info("login success:"+user);
            return Result.ok().put("userId",user.getUser_id());
        }
        return Result.error(10000,"账号或密码错误");
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute(ConstantUtils.USER_SESSION_KEY);
        Object o = request.getSession().getAttribute(ConstantUtils.USER_SESSION_KEY);
        if (o == null) {
            return Result.ok().put("msg", "退出成功");
        }
        return Result.error();
    }

}

