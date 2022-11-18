package org.simplehttp.server.core;

import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.enums.RequestMethod;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.handler.annonation.Handler;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;

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
            BaseServerContext serverContext = server.getServerContext();
            HttpRequest request = serverContext.getRequestParser().parse(serverContext, socketIn);

            String routePath = request.getUrlWrapper().getUrl().getPath();
            RequestMethod method = request.getBody() == null ? RequestMethod.GET : RequestMethod.POST;
            HttpHandler handler = serverContext.getHandler(method, routePath);
            HttpResponse response = handler.handle(request);

            // 处理 Response
        } catch (IOException e) {
            // TODO 日志
        }
    }
}
