package com.lavor.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * 创建Props对象的三种方法与利用Props对象创建Actor的两种方法
 */
public class PropsActor extends UntypedActor {
    private String arg;

    public PropsActor() {
    }

    public PropsActor(String arg) {
        this.arg = arg;
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        createProps();
        createProps();

    }

    /**
     * 创建Props对象的三种方法
     */
    public  void createProps() {
        //直接用来创建MyActor这个Actor，使用无参数的构造方法
        Props props1 = Props.create(PropsActor.class);
        //使用指定构造方法用来创建ActorWithArgs这个Actor，灵活性差
        Props props2 = Props.create(PropsActor.class, ()->new PropsActor("arg"));
        //给构造方法中传递参数来创建ActorWithArgs这个Actor，一般使用这种方法替代props2使用的方法
        Props props3 = Props.create(PropsActor.class, "arg");
    }

    /**
     * 利用Props对象创建Actor的两种方法
     */
    public  void createActor() {
        Props props = Props.create(PropsActor.class);
        //ActorSystem对象可以命名，并且可以重名，
        // 但是它是大对象，一个程序中一般应该只有一个ActorSystem对象
        ActorSystem system = ActorSystem.create("ActorSystem");
        //利用ActorSystem对象创建Actor
        ActorRef actorRef=system.actorOf(props, "PropsActor");
        //在Actor中创建Actor的子Actor
        ActorRef child = getContext().actorOf(props, "PropsActorChild");
    }
}
