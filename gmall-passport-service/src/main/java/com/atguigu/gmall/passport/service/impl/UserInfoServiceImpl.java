package com.atguigu.gmall.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.user.UserInfo;
import com.atguigu.gmall.manager.user.UserInfoService;
import com.atguigu.gmall.passport.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
    public UserInfo login(UserInfo userInfo) {
        String passwd = userInfo.getPasswd();
        String SecretPassword = DigestUtils.md5Hex(passwd);
        UserInfo userInfo1 = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("login_name", userInfo.getLoginName())
                .eq("passwd", SecretPassword));
        return userInfo1;
    }
}

