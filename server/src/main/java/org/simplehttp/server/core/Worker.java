package org.simplehttp.server.core;

import lombok.extern.log4j.Log4j2;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.common.enums.RequestMethod;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.exception.ServerSnapShotException;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.enums.pojo.protocol.HttpRequest;
import org.simplehttp.server.enums.pojo.protocol.HttpResponse;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 工作线程，负责每一个到来请求的处理
 */
@Log4j2
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
        InputStream socketIn = null;
        OutputStream socketOut = null;
        try {
            socketIn = socket.getInputStream();
            socketOut = socket.getOutputStream();
            BaseServerContext serverContext = server.getServerContext();
            HttpRequest request = serverContext.getRequestParser().parse(serverContext, socketIn);


            String routePath = request.getUrlWrapper().getUrl().getPath();
            RequestMethod method = request.getBody() == null ? RequestMethod.GET : RequestMethod.POST;
            HttpHandler handler = serverContext.getHandler(method, routePath);
            HttpResponse response = handler.handle(request);

            // 处理 Response
            serverContext.getResponseBuilder().buildAndWrite(socketOut, response);
        } catch (IOException e) {
            log.error("IO异常");
        }catch(ServerSnapShotException e){
            try {
                server.getServerContext().getResponseBuilder().failAndBuild(socketOut, e);
            } catch (IOException ignored) {}
        } catch (RuntimeException e){
            log.error("运行时异常,{}",e.getMessage());
        }finally {
            // Socket 资源清理
            cleanUp(socketIn, socketOut, socket);
        }
    }

    private void cleanUp(Closeable ... objects){
        for (Closeable o : objects){
            try {
                o.close();
            } catch (IOException ignored) {}
        }
    }
}
