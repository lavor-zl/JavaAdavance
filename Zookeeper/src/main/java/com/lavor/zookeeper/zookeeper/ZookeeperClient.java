package com.lavor.zookeeper.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper客户端连接服务器
 */
public class ZookeeperClient implements Watcher{
    private  CountDownLatch connectedSemaphore = new CountDownLatch( 1 );
    private ZooKeeper zooKeeper;

    /**
     * 创建连接指定服务器的Zookeeper客户端
     * @param host 服务器的主机名+端口号
     * @throws IOException
     * @throws InterruptedException
     */
    public ZookeeperClient(String host) throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper(host,10000,this);
        connectedSemaphore.await();
    }
    public ZooKeeper getZooKeeper(){
        return zooKeeper;
    }
    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    /**
     * 客户端上的监控器,默认情况下只能监控客户端连接状态的变化
     * 通过 getData,exists,getChildren三个方法可以设置监控器为true时,
     * 用该客户端监控器来监控节点或者子节点的变化
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        System.out.println("客户端上的监控器:已经触发了" + watchedEvent.getType() + "事件！");
        //主要作用是保证Zookeeper客户端连接上了服务器之后再让当前线程继续执行
        connectedSemaphore.countDown();
    }
}

