package com.lavor.akka;

import akka.actor.*;
import akka.pattern.PatternsCS;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 消息相关的处理
 * 消息可以是任何类型，并且消息是不可变的
 */
public class MessageActor extends UntypedActor{
    private ActorRef messageActor1;
    private ActorRef messageActor2;

    public MessageActor() {
        //设置接收消息超时时间
        getContext().setReceiveTimeout(Duration.create(10, TimeUnit.SECONDS));
    }

    /**
     * 接收消息
     */
    @Override
    public void onReceive(Object o) throws Throwable {


    }
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("ActorSystem");
        Props props = Props.create(MessageActor.class);
        ActorRef messageActor1=system.actorOf(props,"MessageActor1");
        ActorRef messageActor2=system.actorOf(props,"MessageActor2");

    }

    /**
     * 发送消息的两种方法
     */
    public  void sendMessage(ActorRef messageActor1){
        //ActorRef的tell方法是异步发送消息，无返回
        messageActor1.tell("message",getSelf());
        //PatternsCS.ask方法是异步发送消息，并且可以返回结果
        CompletableFuture<Object> result=PatternsCS.ask(messageActor1,"message",1000).toCompletableFuture();

    }

    /**
     * 转发消息
     */
    public  void forwordMessage(ActorRef messageActor1){
        messageActor1.forward("message",getContext());
    }

    /**
     * 回复消息
     */
    public  void replyMessage(){
        //给最后一条消息的发送者回复消息
        getSender().tell("message", getSelf());
    }

}
