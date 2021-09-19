package com.yk.service;

import com.yk.entity.User;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/18 13:43
 */
public interface UserService {
    /**客户端通过这个接口调用服务端的实现类*/
    User getUserByUserId(Integer id);
    /**给这个服务增加一个功能*/
    boolean insertUserId(User user);
}
