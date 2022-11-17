package org.simplehttp.server.core;

import lombok.Data;
import lombok.experimental.Accessors;
import org.simplehttp.server.core.context.ServerContext;

/**
 * 服务器实体对象
 */
@Accessors(chain = true)
@Data
public class SimpleHttpServer {
    public final String protocol = "http";

    // 监听的服务器套接字端口号，不要设置的太小(1024以上)，否则 Linux 上会出权限问题
    private int port = 9520;

    // 服务器的根目录 eg http://127.0.0.1:7070/simple/index.html 中的 simple，默认为 '/'
    private String contextPath = "/";

    // 服务器别名，如果要去别名先去系统里面注册一下，默认使用 localhost
    private String hostAlias = "localhost";

    /**
     * 绑定的上下文，绑定之前需要自己到上下文中注册一下自己需要的组件
     * @see ServerContext
     */
    private ServerContext serverContext;
}
