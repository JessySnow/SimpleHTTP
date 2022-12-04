package org.simplehttp.server.core;

import lombok.extern.log4j.Log4j2;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.enums.StatusCode;
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
        InputStream socketInStream = null;
        OutputStream socketOutStream = null;
        HttpRequest request = null;
        try {
            socketInStream = socketIn.getInputStream();
            socketOutStream = socketIn.getOutputStream();
            // 解析请求，并将请求添加到 ThreadLocal 中
            request = this.context.parse(socketInStream);
            // 根据请求调用特定的 Handler
            HttpResponse response = this.context.invoke(request);
            // 写出响应
            HttpResponse processedResponse = this.context.response(response);
            this.context.getResponseBuilder().buildAndWrite(socketOutStream,processedResponse);

        }catch (IOException e){
            log.error("IO 异常");
            if(socketOutStream != null){
                try {
                    String routePath = request == null ? "UNKNOWN" : request.getUrlWrapper().getUrl().getPath();
                    String method = request == null ? "UNKNOWN" : request.getBody() == null ? "GET" : "POST";
                    this.context.getResponseBuilder().failAndBuild(socketOutStream,
                            new ServerSnapShotException(e, routePath, method, StatusCode.INTERNAL_SERVER_ERROR));
                } catch (IOException ex) {
                    log.error("客户端IO异常，连接可能已被客户端提前关闭");
                }
            }
        }catch (ServerSnapShotException e){
            log.error(e);
            try {
                this.context.getResponseBuilder().failAndBuild(socketOutStream, e);
            } catch (IOException ex) {
                log.error("客户端IO异常，连接可能已被客户端提前关闭");
            }
        }catch (Exception e){
            log.error("未知的 IO 异常");
        }finally {
            try {
                if(socketInStream != null) {
                    socketInStream.close();
                }
                if(socketOutStream != null) {
                    socketOutStream.close();
                }
            } catch (IOException ignored) {}

            // clean up
            try {
                socketIn.close();
            } catch (IOException e) {
                log.error("套接字关闭异常，端口号: {}", socketIn.getLocalPort());
            }
        }
    }


    private final BaseServerContext context;
    private final Socket socketIn;

    public Worker(Socket socketIn , BaseServerContext context){
        this.socketIn = socketIn;
        this.context = context;
    }

}
