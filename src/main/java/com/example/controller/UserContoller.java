package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.ValidateCodeUtils;
import groovy.util.logging.Slf4j;
import groovyjarjarantlr.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserContoller {
    @Autowired
    private UserService userService;

    //移动端验证
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        //获取手机号
        String phone = user.getPhone();
        //生成随机的4位验证码
        if(!phone.isEmpty()){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云的短信服务api
            //需要将生成的验证码存储到session对
            httpSession.setAttribute(phone,code);
            return R.success("手机验证码发送成功！");
        }
        return R.error("手机验证码发送失败！");
    }


    //移动端登陆
    @PostMapping("/login")
    public R<User> sendMsg(@RequestBody Map map, HttpSession httpSession){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        Object codeInSession = httpSession.getAttribute(phone);
        //进行验证码的比对（页面提交的验证码和session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            //如果比对成功那么登陆成功
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //判断当前手机号是否是新用户，如果是新用户就自动进行注册
            if(user == null){
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
            }
        return R.success(user);
        }
        return R.error("失败");
    }

}
