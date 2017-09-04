package com.lavor.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import akka.pattern.PatternsCS;

/**
 * Actor中常用API
 */
public class APIActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Throwable {

    }

    /**
     * Actor的API使用
     */
    public void useAPI(){
        getSelf();//获取Actor对应的ActorRef
        getSender();//获取Actor接收到最后一条消息的发送者
        supervisorStrategy();//重写该方法可以定义用于监督子Actor的策略
        //暴露了Actor和当前消息的上下文信息，如：
        //利用工厂方法创建子Actor(actorOf)
        //获取Actor所属的Actor系统(system)
        //监督子Actor(getChild,getChildren)
        //监听生命周期(watch)
        //热插拔行为栈(become,unBecome)
        //根据名字或者路径查找现有的Actor(actorSelection),之后调用anchor方法
        getContext();

        //停止Actor
        getContext().stop(getSelf());
        //杀死Actor
        getSelf().tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
        //杀死Actor
        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());

    }
}
