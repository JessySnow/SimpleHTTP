package org.simplehttp.server.core;

import java.net.Socket;
import java.util.Objects;

/**
 * 工作线程，负责每一个到来请求的处理
 */
public class Worker implements Runnable{

    // 获取启动的服务器的实例的引用
    private SimpleHttpServer server;
    // 需要处理的请求 Socket 引用
     private Socket socket;

    public Worker(SimpleHttpServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
    }
}
