package org.simplehttp.server;

import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.handler.impl.EchoHandler;

/**
 * 服务器启动 Demo
 */
public class Bootstrap {
    public static void main(String[] args) {
        // Demo 服务器启动，绑定端口在 9990
        // HostAlias 目前没用，可以不设置
        // contextPath 用来设定服务器能够接受的根路径
        // start 启动服务
        // 如果要优雅地关闭服务器在启动的控制台打一个回车就行，服务器处理完所有的请求后会释放资源并关闭，粗暴一点的话就 ctrl + c
        SimpleHttpServer localhost = new SimpleHttpServer()
                .setPort(9990)
                .setHostAlias("localhost")
                .setContextPath("/test")
                .bindContext(new BaseServerContext());

        // 添加处理器处理器
        localhost.getServerContext().addHandler(EchoHandler.class);

        // 启动服务器
        localhost.start();
    }
}
