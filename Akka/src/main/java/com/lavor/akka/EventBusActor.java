package com.lavor.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.japi.LookupEventBus;

/**
 * akka的EventBus(事件总线)
 * EventBus由Event,Classifier,Subscriber
 * Event是达不到总线上的事件
 * Classifier是用于分配事件给订阅者的分类器
 * Subscriber是允许在事件总线上注册的订阅者
 * Created by zenglei on 17-7-14.
 */
public class EventBusActor extends UntypedActor{
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String){
            System.out.println((String)message);
        }else {
            System.out.println(message);
        }
    }

    public static void main(String[] args){
        ActorSystem system=ActorSystem.create("ActorSystem");
        Props props= Props.create(EventBusActor.class);
        ActorRef actorRef=system.actorOf(props,"EventBusActor");


        MyEventBus myEventBus = new MyEventBus();
        //actorRef使用"time"分类器订阅myEventBus
        myEventBus.subscribe(actorRef, "time");
        myEventBus.publish(new MyEvent("time", System.currentTimeMillis()+""));
        myEventBus.publish(new MyEvent("greetings", "hello"));

        //EventStream充当时间总线的使用方法,直接将符合分类器类型的事件作为消息发给Actor
        system.eventStream().subscribe(actorRef,MyEvent.class);
        system.eventStream().publish(new MyEvent("name", "lavor"));

    }
}
class MyEvent{
    public final String classifier;
    public final String payload;

    public MyEvent(String topic, String payload) {
        this.classifier = topic;
        this.payload = payload;
    }
}
class MyEventBus extends LookupEventBus<MyEvent, ActorRef, String>{

    /**
     * 不同分类器的最大数量
     * @return
     */
    @Override
    public int mapSize() {
        return 128;
    }

    /**
     * 比较订阅者大小
     * @param a
     * @param b
     * @return
     */
    @Override
    public int compareSubscribers(ActorRef a, ActorRef b) {
        return a.compareTo(b);
    }

    /**
     * 从传入事件中提取分类器
     * @param event
     * @return
     */
    @Override
    public String classify(MyEvent event) {
        return event.classifier;
    }

    /**
     * 发布事件时,满足分类器条件的订阅者会调用该方法处理事件
     * @param event
     * @param subscriber
     */
    @Override
    public void publish(MyEvent event, ActorRef subscriber) {
        subscriber.tell(event.payload, ActorRef.noSender());
    }
}
