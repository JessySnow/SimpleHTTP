package org.simplehttp.server.core.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.parser.HttpRequestParser;
import org.simplehttp.server.core.parser.HttpResponseBuilder;
import org.simplehttp.server.core.parser.URLParser;
import org.simplehttp.server.core.session.Session;
import org.simplehttp.server.pojo.protocol.HttpRequest;

import java.util.HashMap;

/**
 * 抽象服务器上下文，提供 HTTP 服务器最核心的功能管理，通过继承这个类来拓展额外的功能
 * @see ServerContext
 * 1. 请求解析
 * 2. 请求路由
 */
public abstract class AbstractServerContext {
    public SimpleHttpServer server = null;

    // 请求暂存
    protected ThreadLocal<HttpRequest> httpRequestThreadLocal;

    // 只读 Map，在服务器启动时进行初始化，后续只会进行读操作从上下文拿 Session 实现功能，启动后不要向这个 SessionMap 中写内容
    protected final HashMap<Class<? extends Session<?,?>>, Session<?,?>> sessionHashMap = new HashMap<>();

    // Http 请求解析器
    @Getter
    protected HttpRequestParser requestParser;
    // Http 响应构造器
    @Getter
    protected HttpResponseBuilder responseBuilder;
    // URL 解析器
    @Getter
    @Setter
//    @Accessors(chain = true)
    protected URLParser urlParser;

    public void putRequest(HttpRequest request){
        this.httpRequestThreadLocal.set(request);
    }

    public HttpRequest getRequest(){
        return httpRequestThreadLocal.get();
    }
}
