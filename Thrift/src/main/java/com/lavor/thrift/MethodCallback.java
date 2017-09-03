package com.lavor.thrift;

import org.apache.thrift.async.AsyncMethodCallback;

/**
 * 远程调用异步返回
 */
public class MethodCallback implements AsyncMethodCallback {
    private Object response = null;
     public Object getResult() {
         // 返回结果值
         return this.response;
     }

    /**
     * 处理服务返回的结果值
     * @param o
     */
    @Override
    public void onComplete(Object o) {
        this.response = o;
        System.out.println("服务调用成功");
    }

    /**
     * 处理调用服务过程中出现的异常
     * @param e
     */
    @Override
    public void onError(Exception e) {
        System.out.println("服务调用失败");
    }
}
