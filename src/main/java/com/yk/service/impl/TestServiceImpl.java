package com.yk.service.impl;

import com.yk.service.TestService;

/**
 * @author yk
 * @version 1.0
 * @date 2021/7/19 15:53
 */
public class TestServiceImpl implements TestService {

    @Override
    public String test(Integer id) {
        System.out.println("客户端调用了test方法 id = "+id);
        return id+"";
    }
}
