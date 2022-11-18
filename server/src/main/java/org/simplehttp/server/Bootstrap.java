package org.simplehttp.server;

import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.ServerContext;

/**
 * 服务器启动 Demo
 */
public class Bootstrap {
    public static void main(String[] args) {
        new SimpleHttpServer()
                .setPort(9990)
                .setHostAlias("localhost")
                .setContextPath("/api")
                .bindContext(new ServerContext())
                .start();
    }
}
