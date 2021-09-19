package com.yk.loadBalance.impl;

import com.yk.loadBalance.LoadBalance;

import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @date 2021/8/13 16:06
 */
public class RoundLoadBalance implements LoadBalance {
    int choose = -1;

    @Override
    public String balance(List<String> addressList) {
        choose++;
        return addressList.get(choose%addressList.size());

    }
}
