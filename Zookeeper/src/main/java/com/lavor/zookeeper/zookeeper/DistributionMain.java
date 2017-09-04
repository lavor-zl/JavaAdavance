package com.lavor.zookeeper.zookeeper;

import com.lavor.zookeeper.zookeeper.queue.DistributedQueue;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class DistributionMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        mutiQueue2();

    }
    /**
     * 集群模式测试分布式锁
     * @throws IOException
     * @throws InterruptedException
     */
    public static void mutiLock() throws IOException, InterruptedException {
        Properties zkProperties=new Properties();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper1.properties"));
        ZookeeperServer.getZooKeeperCluster(zkProperties);
        zkProperties.clear();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper2.properties"));
        ZookeeperServer.getZooKeeperCluster(zkProperties);
        zkProperties.clear();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper3.properties"));
        ZookeeperServer.getZooKeeperCluster(zkProperties);
        zkProperties.clear();
        Thread.sleep(5000);
        final String[] strings=new String[]{"localhost:2181","localhost:2182","localhost:2183"};
        for (int i=0;i<100;i++){
            final int j=i;
            new Thread(new Runnable() {
                public void run() {
                    DistributedLock distributedLock=new DistributedLock(strings[j%3],"2");
                    distributedLock.lock();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (distributedLock!=null){
                        Date date=new Date(System.currentTimeMillis());
                        System.out.println("时间"+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds());
                        distributedLock.unlock();
                    }

                }
            }).start();
        }
    }
    public static void mutiQueue2() throws IOException, InterruptedException {
        Properties zkProperties=new Properties();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper1.properties"));
        ZookeeperServer.getZooKeeperCluster(zkProperties);
        zkProperties.clear();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper2.properties"));
        ZookeeperServer.getZooKeeperCluster(zkProperties);
        zkProperties.clear();
        zkProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper3.properties"));
        ZookeeperServer.getZooKeeperCluster(zkProperties);
        zkProperties.clear();
        Thread.sleep(5000);
        final String[] strings=new String[]{"localhost:2181","localhost:2182","localhost:2183"};
        ZookeeperClient zookeeperClient= null;
        zookeeperClient = new ZookeeperClient(strings[0]);
        ZooKeeper zooKeeper=zookeeperClient.getZooKeeper();
        try {
            zooKeeper.exists("hello",false);
            System.out.println("查看");
        } catch (KeeperException e) {
            System.out.println("查看2");
            e.printStackTrace();
        }finally {
            System.out.println("查看3");
        }
        System.out.println("查看4");
        for (int i=0;i<10;i++){
            final int j=i;
            new Thread(new Runnable() {
                public void run() {
                    ZookeeperClient zookeeperClient= null;
                    try {
                        zookeeperClient = new ZookeeperClient(strings[j % 3]);
                        ZooKeeper zooKeeper=zookeeperClient.getZooKeeper();
                        DistributedQueue distributionQueue = new DistributedQueue(zooKeeper,"hello662443366", ZooDefs.Ids.OPEN_ACL_UNSAFE);
                        System.out.println("offer2");
                        distributionQueue.offer((j+"").getBytes());
                        distributionQueue.remove();
                        System.out.println("offer4");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
