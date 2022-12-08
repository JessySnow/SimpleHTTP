package org.simplehttp.server.handler;

import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;

/**
 * 抽象的处理器
 */
public abstract class HttpHandler {
    public abstract HttpResponse handle(HttpRequest request);
}
