package org.simplehttp.server.core.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.parser.HttpRequestParser;
import org.simplehttp.server.core.parser.HttpResponseBuilder;
import org.simplehttp.server.core.parser.URLParser;
import org.simplehttp.server.core.session.Session;
import org.simplehttp.server.enums.RequestMethod;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.handler.annonation.Handler;
import org.simplehttp.server.pojo.protocol.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 基础服务器上下文，提供 HTTP 服务器最核心的功能管理，通过继承这个类来拓展额外的功能
 * @see ServerContext
 * 1. 请求解析
 * 2. 请求路由
 */
@Log4j2
public class BaseServerContext {
    @Getter
    // 绑定的服务器实例引用
    public SimpleHttpServer server = null;

    // 请求暂存，暂时没用到
    protected ThreadLocal<HttpRequest> httpRequestThreadLocal = new ThreadLocal<>();

    // TODO 包扫描
    // TODO 动态枚举Map加快匹配
    // 缓存的无状态处理器，在类绑定到上下文时被初始化
    // 处理器集合，一级路由按照请求方法进行
    protected HashMap<String, HttpHandler> getHttpHandlerMap = new HashMap<>();
    protected HashMap<String, HttpHandler> postHttpHandlerMap = new HashMap<>();

    // 只读 Map，在服务器启动时进行初始化，后续只会进行读操作从上下文拿 Session 实现功能，启动后不要向这个 SessionMap 中写内容
    protected final HashMap<Class<? extends Session<?,?>>, Session<?,?>> sessionHashMap = new HashMap<>();

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
    public BaseServerContext addHandler(Class<? extends HttpHandler> clazz){
        try {
            HttpHandler httpHandler = clazz.getConstructor().newInstance();
            Handler annotation = clazz.getAnnotation(Handler.class);
            if (annotation.method().equals(RequestMethod.GET)){
                getHttpHandlerMap.put(server.getContextPath() + annotation.routePath(), httpHandler);
            } else if (annotation.method().equals(RequestMethod.POST)){
                postHttpHandlerMap.put(server.getContextPath() + annotation.routePath(), httpHandler);
            }else {
                throw new IllegalArgumentException("不支持的请求方法");
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("实例化处理器失败{}",clazz.getName());
        }catch (IllegalArgumentException e){
            log.error("暂不支持对应请求方法的处理器: {}",clazz.getName());
        }catch (NullPointerException e){
            log.error("实例化处理器失败，请检查处理器注解{}", clazz.getName());
        }
        return this;
    }

    public HttpHandler getHandler(RequestMethod method, String routePath){
        if (method.equals(RequestMethod.GET)){
            return getHttpHandlerMap.get(routePath);
        }
        return this.postHttpHandlerMap.get(routePath);
    }
}
