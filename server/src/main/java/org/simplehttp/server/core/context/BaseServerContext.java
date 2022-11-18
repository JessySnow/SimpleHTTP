package org.simplehttp.server.core.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.parser.HttpRequestParser;
import org.simplehttp.server.core.parser.HttpResponseBuilder;
import org.simplehttp.server.core.parser.URLParser;
import org.simplehttp.server.core.session.Session;
import org.simplehttp.server.handler.Handler;
import org.simplehttp.server.pojo.protocol.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 基础服务器上下文，提供 HTTP 服务器最核心的功能管理，通过继承这个类来拓展额外的功能
 * @see ServerContext
 * 1. 请求解析
 * 2. 请求路由
 */
public class BaseServerContext {
    @Getter
    // 绑定的服务器实例引用
    public SimpleHttpServer server = null;

    // 请求暂存
    protected ThreadLocal<HttpRequest> httpRequestThreadLocal = new ThreadLocal<>();

    // 只读 Map，在服务器启动时进行初始化，后续只会进行读操作从上下文拿 Session 实现功能，启动后不要向这个 SessionMap 中写内容
    protected final HashMap<Class<? extends Session<?,?>>, Session<?,?>> sessionHashMap = new HashMap<>();

    // TODO 包扫描
    // 缓存的无状态处理器，在类绑定到上下文时被初始化
    private final HashSet<Handler> stateLessHandlerSet = new HashSet<>();

    // Http 请求解析器
    @Getter
    protected HttpRequestParser requestParser = new HttpRequestParser();
    // Http 响应构造器
    @Getter
    protected HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
    // URL 解析器
    @Getter
    @Setter
    @Accessors(chain = true)
    protected URLParser urlParser = new URLParser();


    // 将当前工作线程的请求实体引用放置到上下文中
    public void putRequest(HttpRequest request){
        this.httpRequestThreadLocal.set(request);
    }
    // 从上下文获取当前工作线程的请求实体引用
    public HttpRequest getRequest(){
        return httpRequestThreadLocal.get();
    }

    // 绑定一个服务器
    public void bindServer(SimpleHttpServer server){
        this.server = server;
    }

    // 添加一个处理器
    public HashSet<Handler> addHandler(Class<? extends Handler> clazz){
        try {
            Handler handler = clazz.getConstructor().newInstance();
            this.stateLessHandlerSet.add(handler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO 提示有 Handler 没有加载成功，但是不结束加载服务器的其他组件
        }
        return this.stateLessHandlerSet;
    }
}
