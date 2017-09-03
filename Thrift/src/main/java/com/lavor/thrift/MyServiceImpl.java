package com.lavor.thrift;

import org.apache.thrift.TException;

/**
 * Created by shitian on 2017-09-03.
 */
public class MyServiceImpl implements MyService.Iface{
    @Override
    public void plus(int x, int y) throws TException {
        System.out.println("plus方法执行");

    }

    @Override
    public int minus(int x, int y) throws TException {
        System.out.println("minus方法执行");
        return x-y;
    }
}
