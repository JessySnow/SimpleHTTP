package org.simplehttp.server.handler;

import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

/**
 * 编写业务逻辑的处理器，由服务器的上下文负责处理你需要的参数
 * 你只需要编写对应请求路径的处理逻辑就可以了
 */
public interface Handler extends Function<HttpRequest,HttpResponse> {
    @Override
    default HttpResponse apply(HttpRequest request){
        return handle(request);
    }

    // 编写自己的处理器时，只需要覆盖这个方法，上一个方法是给框架用的
    HttpResponse handle(HttpRequest request);
}
