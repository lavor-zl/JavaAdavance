package com.lavor.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**在实际使用akka中,可能会需要定时或重复的发送消息给某些Actor.要处理这类的问题,
 * 除了直接使用JAVA的API或Quartz显式的重复调用ActorRef.tell外,akka还提供了一个简单的Scheduler.
 */
public class SchedulerActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Throwable {

    }
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("ActorSystem");
        Props props = Props.create(SchedulerActor.class);
        ActorRef actorRef=system.actorOf(props,"SchedulerActor");
        //定时发送消息，scheduleOnce(延迟时间,目标Actor,消息,调度器,发送者)
        system.scheduler().scheduleOnce(Duration.create(5, TimeUnit.SECONDS),
                actorRef,"message",system.dispatcher(), ActorRef.noSender());
        //重复发送消息，schedule(第一次调用时间,间隔时间,目标Actor,消息,调度器,发送者)
        system.scheduler().schedule(Duration.Zero(),Duration.create(1, TimeUnit.SECONDS),
                actorRef,"1111",system.dispatcher(),ActorRef.noSender());


    }
}
