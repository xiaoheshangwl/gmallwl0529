package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.atguigu.gmall.manager.user.UserInfo;
import com.atguigu.gmall.manager.user.UserInfoService;
import com.atguigu.gmall.passport.utils.JwtUtils;
import constant.CookieConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class LoginController {
    @Reference
    UserInfoService  userInfoService;
    /**
     * 要登陆去http://www.gmallsso.com/loginPage.html制证
     * 去往登陆页面
     * @return
     */


    @RequestMapping("/login")
    public String login(UserInfo userInfo, String originUrl,
                        @CookieValue(name= CookieConstant.SSO_COOKIE_NAME,required = false) String cookieValue
                        ,HttpServletResponse response){

        //如果已经登陆过了，重定向返回
        if(!StringUtils.isEmpty(cookieValue)){
            if(originUrl!=null){
                return "redirect:"+originUrl+"?token="+cookieValue;
            }else{
                //去首页
                return "redirect:http://www.gmall.com";
            }

        }else{
            //第一次进来没有gmallsso的cookie
            if(StringUtils.isEmpty(userInfo.getLoginName()) ){
                return "index";
            }else{
                UserInfo loginInfo = userInfoService.login(userInfo);
                Map<String,Object> body = new HashMap<>();
                body.put("id",loginInfo.getId());
                body.put("nickName",loginInfo.getLoginName());
                body.put("nickName",loginInfo.getName());
                body.put("headImg",loginInfo.getHeadImg());
                body.put("email",loginInfo.getEmail());
                if(loginInfo !=null){
                    //制证
                    String newToken = JwtUtils.createJwtToken(body);
                    //保存令牌
                    Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, newToken);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    //交给用户
                    log.info("token的值是{}",newToken);
                    //重定向到之前的那个页面
                    if(originUrl!=null){
                        return "redirect:"+originUrl+"?token="+newToken;
                    }else{
                        //去首页
                        return "redirect:http://www.gmall.com";
                    }

                }else {
                    return "index";
                }
            }
        }

        //以后进来有gmallsso 的cookie
        //检查cookie的合法性和时效性，看gmallsso整个cookie的值token还有没有，
        // 有就带上token的值，返回。没有就是已经超时了，需要重新登陆



    }
    @ResponseBody
@RequestMapping("/confirmToken")
    public String confirmToken(String token){
    boolean b = JwtUtils.confirmJwtToken(token);
        System.out.println(b);
    return b?"ok":"error";
}
}
