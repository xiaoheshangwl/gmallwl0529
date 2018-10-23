package com.atguigu.gmall.manager.user;

public interface UserInfoService {
    /**
     * 登陆
     * 按照带来的账号密码 查询客户详细信息
     * @param userInfo
     * @return
     */
    public UserInfo login(UserInfo userInfo);
}
