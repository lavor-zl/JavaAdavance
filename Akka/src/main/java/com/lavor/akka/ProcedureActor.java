package com.lavor.akka;

import akka.actor.*;
import akka.japi.Procedure;

/**
 * 在actor运行过程中，可能会有多种状态，各个状态间可能会存在切换的情况，
 * 其实切换状态，就是出接收消息的处理方法的改变
 * akka已经帮我们考虑到这种情况情况的处理:Procedure.
 * Created by zenglei on 17-7-14.
 */
public class ProcedureActor extends UntypedActor{

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object o) throws Exception {
            System.out.println("切换状态到happy来处理消息！消息是："+(String)o);
            //解除状态切换，从下次开始消息还是由原来的方式处理
            getContext().unbecome();
        }
    };

    Procedure<Object> angray = new Procedure<Object>() {
        @Override
        public void apply(Object o) throws Exception {
            System.out.println("切换状态到angry来处理消息！消息是："+(String)o);
            getContext().unbecome();
        }
    };


    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof String) {
            System.out.println("原始状态来处理消息！消息是："+(String)o);
            if (o.equals("happy")) {
                //下次接受消息使用happy处理
                getContext().become(happy);
            }else if (o.equals("angry")){
                getContext().become(angray);
            }
        }
    }



    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("ActorSystem");
        ActorRef actorRef = system.actorOf(Props.create(ProcedureActor.class), "ProcedureActor");

        actorRef.tell("happy", ActorRef.noSender());
        actorRef.tell("angry", ActorRef.noSender());
        actorRef.tell("cold", ActorRef.noSender());
        actorRef.tell("angry", ActorRef.noSender());
        actorRef.tell("cold", ActorRef.noSender());

    }
}
