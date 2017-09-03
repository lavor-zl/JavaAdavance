package com.lavor.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.server.AbstractNonblockingServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by shitian on 2017-09-03.
 */
public class MyServiceServer {
    /**
     * 启动 Thrift 服务器
     * 服务器的启动需要三个参数：协议（传输协议）、传输（传输方式）、处理器
     * 协议种类繁多主要有以下几种：
     * TBinaryProtocol：二进制编码格式进行数据传输
     * TCompactProtocol：高效率的、密集的二进制编码格式进行数据传输
     * TJSONProtocol：使用JSON的数据编码协议进行数据传输。
     * TSimpleJSONProtocol：只提供 JSON 只写的协议，适用于通过脚本语言解析
     * 传输（有服务器端与客户端之分）种类繁多主要有以下几种：
     * TSocket：使用阻塞式 I/O 进行传输，是最常见的模式
     * TFramedTransport：使用非阻塞方式，按块的大小进行传输，类似于 Java 中的 NIO
     * TNonblockingTransport：使用非阻塞方式，用于构建异步客户端
     * 处理器是实现thrift中服务接口的类
     * 服务器也有好几种类型：
     * TSimpleServer：单线程服务器端使用标准的阻塞式 I/O，一般配合TSocket使用
     * TThreadedServer：多线程服务器端使用标准的阻塞式 I/O，一般配合TSocket使用
     * TThreadPoolServer：线程池服务器端使用标准的阻塞式 I/O，一般配合TSocket使用
     * TNonblockingServer：多线程服务器端使用非阻塞式 I/O，该类型必须与TNonblockingTransport或者TFramedTransport使用
     * @param args
     */
    public static void main(String[] args) throws TTransportException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sync();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                async();
            }
        }).start();
    }
    public static void sync(){
        try {
            // 设置服务端口为 7911
            TServerSocket serverTransport = new TServerSocket(7911);
            // 设置协议工厂为 TBinaryProtocol.Factory
            TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();

            // 关联处理器与 MyService 服务的实现
            TProcessor processor = new MyService.Processor(new MyServiceImpl());
            TThreadPoolServer.Args tArgs=new TThreadPoolServer.Args(serverTransport);
            tArgs.protocolFactory(proFactory);
            tArgs.processor(processor);
            TServer server = new TThreadPoolServer(tArgs);
            System.out.println("Start server on port 7911...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
    public static void async(){
        TNonblockingServerTransport serverTransport;
        try {
            serverTransport = new TNonblockingServerSocket(10005);
            MyService.Processor processor = new MyService.Processor(new MyServiceImpl());
            TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
            AbstractNonblockingServer.AbstractNonblockingServerArgs tArgs =new TNonblockingServer.Args(serverTransport);
            tArgs.protocolFactory(proFactory);
            tArgs.processor(processor);
            TServer server = new TNonblockingServer(tArgs);
            System.out.println("Start server on port 10005 ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
