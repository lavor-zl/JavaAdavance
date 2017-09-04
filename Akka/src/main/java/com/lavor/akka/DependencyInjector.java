package com.lavor.akka;

import akka.actor.*;

/**
 * 利用依赖注入为Actor注入参数，并且创建Actor
 */
public class DependencyInjector implements IndirectActorProducer {
    final Object applicationContext;
    public DependencyInjector(Object applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 得到Actor的类型
     * @return
     */
    @Override
    public Class<? extends Actor> actorClass() {
        return PropsActor.class;
    }

    /**
     * 得到Actor的方法，使用了怎样的构造方法得到的Actor实例
     */
    @Override
    public PropsActor produce() {
        PropsActor result;
        result = new PropsActor((String) applicationContext);
        return result;
    }
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("ActorSystem");
        //利用依赖注入创建Actor
        ActorRef actorRef = system.actorOf(
                Props.create(DependencyInjector.class, "Actor"),"Actor");
    }

}
