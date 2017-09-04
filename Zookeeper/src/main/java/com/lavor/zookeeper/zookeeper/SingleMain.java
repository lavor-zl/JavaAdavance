package com.lavor.zookeeper.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Properties;

/**
 * 单机模式运行Zookeeper的主类
 * 客户端中的监控器默认只能监控客户端状态的变化:EventType.None
 * exists方法可以设置监听节点的三种事件(创建节点,修改节点数据,删除节点):
 * EventType.NodeCreated,EventType.NodeDataChanged,EventType.NodeDeleted
 * getData方法可以设置监听节点的两种事件(修改节点数据,删除节点):
 * EventType.NodeDataChanged,EventType.NodeDeleted
 * getData方法在节点不存在时设置监控器,那么该节点上的所有监控器都将不起作用,并且不能再在该节点上设置监控器了
 * Zookeeper中的Watcher是一次性触发的(是针对节点的一次性触发),也就是一个Watcher只能监听到一次节点的事件变化
 * 如果Watcher同时监听多个节点,那么它可以从每个节点那里收到一次事件变化通知
 * 节点上设置同一个监控器多次时,只能起到一次监控作用,但是事件通知发生后在设置另一个监控器,那么监控器会重新起作用
 * Watcher接收事件通知是异步的
 * 客户端上的监控器监听客户端变化的事件不算在监听节点事件中
 */
public class SingleMain {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Properties zkProperties=new Properties();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zoo.properties"));
        ZookeeperServer.getZooKeeperSingle(zkProperties);
        //创建连接Zookeeper服务器的客户端
        ZookeeperClient zookeeperClient=new ZookeeperClient("localhost:2180");
        //获取Zookeeper客户端
        final ZooKeeper zooKeeper=zookeeperClient.getZooKeeper();
        String path="/nodePath";
        String path2="/nodePath2";
        //判断节点是否存在,并添加监控器(此时监控器为客户端上设置的监控器)
        //不管节点此时存在与否,监控器都会被添加成功
        zooKeeper.exists(path, true);
        zooKeeper.exists(path2, true);
        zooKeeper.getData(path, true,null);
        //创建节点(节点路径,节点保存的数据,节点的ACL,节点类型),节点信息是保存在服务器中的
        zooKeeper.create(path,"data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.create(path2,"data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //获取节点数据,并添加监控器,节点存在则添加监控器成功,
        // 节点不存在时取消所有在节点上设置的监控器,并且该节点上再也不能设置监控器了
        zooKeeper.getData(path, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("新的监控器:已经触发了" + watchedEvent.getType() + "事件！");
            }
        }, null);
        //注意:同一种类型的事件,只给监控该节点的所有监控器发一次通知,这就是一次性触发
        //-1表示最新版本
        zooKeeper.setData(path, "setData".getBytes(), -1);
        zooKeeper.getData(path, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("又一个新的监控器:已经触发了" + watchedEvent.getType() + "事件！");
            }
        }, null);
        zooKeeper.exists(path,true);
        zooKeeper.setData(path, "setData".getBytes(), -1);
        zooKeeper.exists(path,true);
        zooKeeper.setData(path, "setData".getBytes(), -1);
        zooKeeper.exists(path, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("另个新的监控器:已经触发了" + watchedEvent.getType() + "事件！");
            }
        });
        zooKeeper.delete(path,-1);
        zooKeeper.delete(path2,-1);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
