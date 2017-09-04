package com.lavor.akka;

import akka.actor.UntypedActor;

/**
 * 定义Actor类：通过继承UntypedActor并实现onReceive方法
 */
public class DefineActor extends UntypedActor{
    /**
     * 处理传递过来的消息，所有传递过来的消息都是用同步方法处理的
     * 消息可以是任何类型，并且是不可变的
     */
    @Override
    public void onReceive(Object o) throws Throwable {

    }
}
