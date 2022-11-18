package org.simplehttp.server.core;

import org.simplehttp.server.core.context.AbstractServerContext;
import org.simplehttp.server.pojo.protocol.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 工作线程，负责每一个到来请求的处理
 */
public class Worker implements Runnable{

    // 获取启动的服务器的实例的引用
    private final SimpleHttpServer server;
    // 需要处理的请求 Socket 引用
    private final Socket socket;

    public Worker(SimpleHttpServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream socketIn = socket.getInputStream();
            OutputStream socketOut = socket.getOutputStream();
            AbstractServerContext serverContext = server.getServerContext();
            HttpRequest parse = serverContext.getRequestParser().parse(serverContext, socketIn);
            System.out.println(parse);
        } catch (IOException e) {
            // TODO 日志
        }
    }
}
