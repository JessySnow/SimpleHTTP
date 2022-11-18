package org.simplehttp.server.core;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.simplehttp.server.core.context.AbstractServerContext;
import org.simplehttp.server.core.context.ServerContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器实体对象
 */
@Accessors(chain = true)
public class SimpleHttpServer {
    public final String protocol = "http";

    // 监听的服务器套接字端口号，不要设置的太小(1024以上)，否则 Linux 上会出权限问题
    @Accessors(chain = true)
    @Getter
    @Setter
    private int port = 9520;

    // 服务器的根目录 eg http://127.0.0.1:7070/simple/index.html 中的 simple，默认为 '/'
    @Accessors(chain = true)
    @Getter
    @Setter
    private String contextPath = "/";

    // 服务器别名，如果要去别名先去系统里面注册一下，默认使用 localhost
    @Accessors(chain = true)
    @Getter
    @Setter
    private String hostAlias = "localhost";

    /**
     * 绑定的上下文，绑定之前需要自己到上下文中注册一下自己需要的组件
     * @see ServerContext
     */
    private AbstractServerContext serverContext;

    /**
     * 固定大小的线程池
     * 默认大小 5
     */

    private ExecutorService fixedExecutorPool;
    @Accessors(chain = true)
    @Getter
    @Setter
    private static int DEFAULT_POOL_SIZE = 5;

    public SimpleHttpServer(){
        fixedExecutorPool = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
    }

    public SimpleHttpServer(AbstractServerContext context){
        this();
        this.serverContext = context;
    }

    /**
     * 启动服务器
     * TODO 添加日志支持
     * TODO 优雅关闭
     */
    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket accept = serverSocket.accept();
                fixedExecutorPool.execute(new Worker(this, accept));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器和上下文互相绑定
     */
    public SimpleHttpServer bindContext(AbstractServerContext context){
        this.serverContext = context;
        context.bindServer(this);
        return this;
    }
}
