package com.lavor.zookeeper.zookeeper;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

import java.io.IOException;
import java.util.Properties;

/**
 * Zookeeper服务器的启动(但是一般我们不会在Java中启动服务器,而会通过命令行启动服务器)
 */
public class ZookeeperServer {
    /**
     * 以单机模式启动Zookeeper服务器
     * @param zkProperties 保存Zookeeper服务器启动的各种属性
     *                     相当于命令行下从conf目录中的cfg文件中获取的配置信息
     * @throws IOException
     */
    public static void getZooKeeperSingle(Properties zkProperties) throws IOException {
        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(zkProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final ZooKeeperServerMain zooKeeperServer = new ZooKeeperServerMain();
        final ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumConfiguration);
        new Thread(){
            public void run(){
                try {
                    zooKeeperServer.runFromConfig(configuration);

                    System.out.println("ZooKeeper单机服务器启动成功");
                } catch (IOException e) {
                    System.out.println("ZooKeeper单机服务器启动失败");
                    e.printStackTrace(System.err);
                }
            }
        }.start();

    }

    /**
     * 以集群模式启动一个Zookeeper服务器
     * @param zkProperties 保存Zookeeper服务器启动的各种属性
     *                     相当于命令行下从conf目录中的cfg文件中获取的配置信息
     * @throws IOException
     */
    public static void getZooKeeperCluster(Properties zkProperties) throws IOException {
        final QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(zkProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final QuorumPeerMain zooKeeperServer = new QuorumPeerMain();
        new Thread() {
            public void run() {
                try {
                    zooKeeperServer.runFromConfig(quorumConfiguration);
                    System.out.println("ZooKeeper集群服务器启动成功");
                } catch (IOException e) {
                    System.out.println("ZooKeeper集群服务器启动失败");
                    e.printStackTrace(System.err);
                }
            }
        }.start();
    }
}