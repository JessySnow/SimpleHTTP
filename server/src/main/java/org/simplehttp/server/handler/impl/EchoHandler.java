package org.simplehttp.server.handler.impl;

import org.simplehttp.server.enums.RequestMethod;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;

/**
 * Handler 示例
 * 使用 Http协议 实现的 Echo 服务
 */
@org.simplehttp.server.handler.annonation.Handler(method = RequestMethod.GET, routePath = "/echo")
public class EchoHandler implements HttpHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        return null;
    }
}
