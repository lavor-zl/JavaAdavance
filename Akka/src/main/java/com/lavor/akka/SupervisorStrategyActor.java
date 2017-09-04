package com.lavor.akka;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by zenglei on 17-7-13.
 */
public class SupervisorStrategyActor extends UntypedActor{
    @Override
    public void onReceive(Object o) throws Throwable {

    }

    /**
     * 重写supervisorStrategy方法，来得到当前Actor的监督策略
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return createSupervisorStrategy();
    }

    /**
     * 创建监督策略：针对不同异常进行不同处理
     */
    public SupervisorStrategy createSupervisorStrategy(){
        //OneForOneStrategy对待Actor的所有孩子采用相同的策略
        SupervisorStrategy strategy =
                new OneForOneStrategy(10, Duration.create(1, TimeUnit.MINUTES), DeciderBuilder.
                        match(ArithmeticException.class, e -> resume()).
                        match(NullPointerException.class, e -> restart()).
                        match(IllegalArgumentException.class, e -> stop()).
                        matchAny(o -> escalate()).build());
        return strategy;


    }
}
