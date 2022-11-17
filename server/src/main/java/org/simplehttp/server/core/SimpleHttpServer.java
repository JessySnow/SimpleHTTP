package org.simplehttp.server.core;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务器实体对象
 */
@Accessors(chain = true)
@Data
public class SimpleHttpServer {
    // 监听的服务器套接字端口号，不要设置的太小(1024以上)，否则 Linux 上会出权限问题
    private int port = 9520;

    // 服务器的根目录 eg http://127.0.0.1:7070/simple/index.html 中的 simple，默认留空
    private String contextPath = "";
}
