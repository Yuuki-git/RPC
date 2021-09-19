package com.yk.service.impl;

import com.yk.entity.User;
import com.yk.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/18 13:45
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户");
        // 模拟从数据库中取用户的行为
        Random random = new Random();
        User user = new User(id,UUID.randomUUID().toString(),random.nextBoolean());
        return user;
    }

    @Override
    public boolean insertUserId(User user) {
        System.out.println("插入数据成功:"+user);
        return true;
    }
}
