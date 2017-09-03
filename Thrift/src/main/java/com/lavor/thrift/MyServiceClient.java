package com.lavor.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;

import java.io.IOException;

/**
 * Created by shitian on 2017-09-03.
 */
public class MyServiceClient {

    public static void main(String[] args) {
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

    /**
     * 同步调用 MyService 服务，客户端与服务器端通信时，两者所使用的协议与传输必须相同,
     * 否则方法调用出错或者方法返回值获取出错
     * 客户端只需要配置传输与协议即可
     */
    public static void sync() {
        try {
            // 设置调用的服务地址为本地，端口为 7911
            TTransport transport = new TSocket("localhost", 7911);
            transport.open();
            // 设置传输协议为 TBinaryProtocol
            TProtocol protocol = new TBinaryProtocol(transport);
            MyService.Client client = new MyService.Client(protocol);
            // 调用服务的 plus 方法
            client.plus(2, 8);
            int number = client.minus(82, 2);
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步调用 MyService 服务，客户端与服务器端通信时，两者所使用的协议与传输必须相同，
     * 否则方法调用出错或者方法返回值获取出错
     * 客户端只需要配置传输与协议即可
     */
    public static void async() {
        try {
            TAsyncClientManager clientManager = new TAsyncClientManager();
            TNonblockingTransport transport = new TNonblockingSocket("localhost", 10005);
            TProtocolFactory proFactory = new TBinaryProtocol.Factory();
            MyService.AsyncClient asyncClient = new MyService.AsyncClient(proFactory, clientManager, transport);
            System.out.println("Client calls .....");
            MethodCallback callBack = new MethodCallback();
            asyncClient.minus(8, 2, callBack);
            Object res = callBack.getResult();
            //这里用while是等待异步处理完毕，然后获取结果值
            while (res == null) {
                res = callBack.getResult();
            }
            //输出计算结果，MyService.AsyncClient.minus_call的getResult()方法会将Object类型转换成远程服务方法原本的返回数据类型
            System.out.println(((MyService.AsyncClient.minus_call) res).getResult());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
