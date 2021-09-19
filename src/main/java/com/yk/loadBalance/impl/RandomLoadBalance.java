package com.yk.loadBalance.impl;

import com.yk.loadBalance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/13 16:03
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String balance(List<String> addressList) {
        int choose = new Random().nextInt(addressList.size());
        System.out.println("选择"+choose+"服务器");
        return addressList.get(choose);
    }
}
