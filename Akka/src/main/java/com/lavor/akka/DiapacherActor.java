package com.lavor.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

/**
 * 在akka中,actor之间都是通过消息的传递来完成彼此的交互的.
 * 而当Actor的数量比较多后,彼此之间的通信就需要协调,从而能更好的平衡整个系统的执行性能.
 * 在akka中,负责协调Actor之间通信的就是Dispatcher.它在自己独立的线程上不断的进行协调,
 * 把来自各个Actor的消息分配到执行线程上.
 * akka中有三种Dispatcher：
 * Dispatcher（默认的，非阻塞），PinnedDispatcher（阻塞)，CallingThreadDispatcher（测试用的）
 */
public class DiapacherActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof String){
            System.out.println((String)o+":"+Thread.currentThread().toString());
        }

    }
    public static void main(String[] args){
        //加载resource中的akka.conf文件中的demo部分创建ActorSystem
        ActorSystem system=ActorSystem.create("ActorSystem",
                ConfigFactory.load("akka").getConfig("demo"));
        //利用指定Dispatcher来创建Props,利用该Props创建的Actor就会使用其中配置的Dispatcher
        Props props = Props.create(DiapacherActor.class).withDispatcher("easy-dispatcher");
        //100个Actor处理接收到的消息，所使用线程却最多只有10个，所以说Actor比线程级别更低
        for (int i=0;i<100;i++){
            ActorRef actorRef = system.actorOf(props, "ActorRef"+i);
            for (int j=0;j<100;j++){
                actorRef.tell(j+"",ActorRef.noSender());
            }
        }

    }
}
