package com.lavor.akka;

import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Inbox（信箱）的使用，Inbox给Actor传递消息，Inbox监听Actor
 */
public class InboxActor extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof String){
            if (o.equals("start")){
                System.out.println("Inbox start!");
                //Actor本身给消息的发送者发送消息,tell是异步发送消息
                //A.tell(msg,B)是B给A发消息，而不是字面理解的A给B发消息
                getSender().tell("start start", getSelf());
            }else if (o.equals("stop")){
                System.out.println("Inbox stop!");
                getSender().tell("stop stop", getSelf());
                //关闭Actor
                getContext().stop(getSelf());
            }{
                unhandled(o);
            }

        }else {
            unhandled(o);
        }
    }

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("ActorSystem");
        Inbox inbox = Inbox.create(system);
        Props props = Props.create(InboxActor.class);
        ActorRef inboxActor=system.actorOf(props,"InboxActor");
        //Inbox给Actor发送消息
        inbox.send(inboxActor, "start");
        inbox.send(inboxActor, "stop");
        //Inbox监听Actor
        inbox.watch(inboxActor);
        while(true){
            try {
                //获取Inbox1秒中内获得的消息，每次只能获得最前面的一条消息
                Object message=inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                if (message instanceof Terminated){
                    System.out.println("Inbox监听到Actor停止了");
                }
                //Inbox监听到Actor停止了
                else if (message instanceof String){
                    System.out.println("Inbox收到Actor传递的消息");
                }else {
                    break;
                }
            } catch (TimeoutException e) {

            }
        }

    }
}
