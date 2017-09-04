package com.lavor.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 在真实的情况中,通常针对某一种消息,会启动很多个相同的Actor来进行处理.
 * 当然,你可以在程序中循环的启动很多个相同的Actor来实现,
 * 但是这就牵涉到Actor任务的平衡,Actor个数的维护等等,比较的麻烦.
 * 因此,在akka中存在一种特殊的Actor,即Router.akka通过Router机制,
 * 来有效的分配消息给actor来完成工作.而在akka中,被Router管理的actor被称作Routee.
 * akka有8中内置的Router（路由）
 * Created by zenglei on 17-7-14.
 */
public class RouterActor extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Throwable {

    }

    /**
     * 在Actor内部使用Router
     * 这种方式是通过akka提供的API,手动的创建Router对象,然后调用addRoutee方法手动的添加Actor
     * (需要注意,每一次调用addRoutee都会返回一个新的Router对象),然后通过route来发送消息.
     */
    public void useRouterInside() {
        //以RoundRobinRoutingLogic方式创建Router
        Router router = new Router(new RoundRobinRoutingLogic());
        List<ActorRef> actors = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ActorRef actorRef = getContext().actorOf(Props.create(RouterActor.class), "RouterActor" + i);
        }
        for (ActorRef actor : actors) {
            //每一次调用addRoutee（添加Actor到路由中）都会返回一个新的Router对象,我们应该使用新的对象
            router = router.addRoutee(actor);
        }
        //给Router（路由）发送一条消息，Router会根据自己的路由策略把消息发到某些Actor中
        router.route("RouterMessage", ActorRef.noSender());

    }

    /**
     * 在Actor外面使用Router
     * 这种方式是通过创建一个RouteActor来使用路由.RouteActor和一般的Actor没有什么不同,
     * 区别在于它没有什么业务逻辑,在创建它的时候,它会创建N个具备业务逻辑的子Actor.
     * 当它接收到消息后,会把消息转发给它的某个子Actor.
     */
    public static void useRouterOutside() {
        //加载resource中的application.conf文件(默认的)中的development部分创建ActorSystem
        ActorSystem system = ActorSystem.create("ActorSystem",
                ConfigFactory.load().getConfig("development"));
        Props props = Props.create(RouterActor.class).withRouter(new FromConfig());
        //"router"这个名字需要与FromConfig配置中的路由路径对应
        ActorRef router = system.actorOf(props, "router");

    }

    public static void main(String[] args) {
        useRouterOutside();
    }
}
