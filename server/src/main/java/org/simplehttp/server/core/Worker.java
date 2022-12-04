package org.simplehttp.server.core;

import lombok.extern.log4j.Log4j2;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.enums.pojo.protocol.HttpRequest;
import org.simplehttp.server.enums.pojo.protocol.HttpResponse;
import org.simplehttp.server.exception.ServerSnapShotException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 工作线程的工作
 *  - 资源获取
 *  - 方法调用
 *  - 资源清理
 * 不要在这里写任何的业务逻辑
 */
@Log4j2
public class Worker implements Runnable{

    @Override
    public void run() {
        try {
            InputStream socketInStream = socketIn.getInputStream();
            OutputStream socketOutStream = socketIn.getOutputStream();

            // 解析请求，并将请求添加到 ThreadLocal 中
            HttpRequest request = this.context.parse(socketInStream);
            // 根据请求调用特定的 Handler
            HttpResponse response = this.context.invoke(request);
            // 写出响应
            HttpResponse processedResponse = this.context.response(response);
            this.context.getResponseBuilder().buildAndWrite(socketOutStream,response);

            // clean up
            socketInStream.close();
            socketOutStream.close();
            socketIn.close();
        }catch (IOException e){
        }catch (ServerSnapShotException e){
        }
    }


    private BaseServerContext context;
    private Socket socketIn;

    public Worker(Socket socketIn , BaseServerContext context){
        this.socketIn = socketIn;
        this.context = context;
    }

}
