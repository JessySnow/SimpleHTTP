package org.simplehttp.server;

import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.ServerContext;
import org.simplehttp.server.handler.impl.EchoHandler;

/**
 * 服务器启动 Demo
 */
public class Bootstrap {
    public static void main(String[] args) {
        // Demo 服务器启动，绑定端口在 9990
        // HostAlias 目前没用，可以不设置
        // contextPath 用来设定服务器能够接受的根路径
        // bindContext 这个直接绑定 ServerContext 即可，如果有功能要扩展，继承这个 Context 写到子类里面即可
        // start 启动服务
        // 如果要优雅地关闭服务器在启动的控制台打一个回车就行，服务器处理完所有的请求后会释放资源并关闭，粗暴一点的话就 ctrl + c
        new SimpleHttpServer()
                .setPort(9990)
                .setHostAlias("localhost")
                .setContextPath("/api")
                .bindContext(new ServerContext().addHandler(EchoHandler.class))
                .start();
    }
}
