package org.simplehttp.server.core.context;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.simplehttp.common.enums.RequestMethod;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.parser.HttpRequestParser;
import org.simplehttp.server.core.parser.HttpResponseBuilder;
import org.simplehttp.server.core.parser.URLParser;
import org.simplehttp.server.core.route.CGIRouter;
import org.simplehttp.server.core.route.Router;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.enums.pojo.protocol.HttpRequest;
import org.simplehttp.server.enums.pojo.protocol.HttpResponse;
import org.simplehttp.server.exception.ServerSnapShotException;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.handler.annonation.Handler;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Function;

/**
 * 基础服务器上下文，提供 HTTP 服务器最核心的功能管理，通过继承这个类来拓展额外的功能
 */
@Log4j2
public class BaseServerContext implements Function<Socket, Void> {

    @Getter
    // 绑定的服务器实例引用
    public SimpleHttpServer server = null;

    // 缓存的无状态处理器，在类绑定到上下文时被初始化
    // 处理器集合，一级路由按照请求方法进行
    @Getter
    protected HashMap<String, HttpHandler> getHttpHandlerMap = new HashMap<>();
    @Getter
    protected HashMap<String, HttpHandler> postHttpHandlerMap = new HashMap<>();

    // Http 请求解析器
    protected HttpRequestParser requestParser = new HttpRequestParser(this);
    // Http 响应构造器
    protected HttpResponseBuilder responseBuilder = new HttpResponseBuilder(this);
    // URL 解析器
    @Getter
    protected URLParser urlParser = new URLParser(this);
    // CGI风格路径路由器
    protected Router router = new CGIRouter(this);

    // 绑定一个服务器
    public void bindServer(SimpleHttpServer server){
        this.server = server;
    }

    // 添加一个处理器
    public BaseServerContext addHandler(Class<? extends HttpHandler> clazz){
        try {
            HttpHandler httpHandler = clazz.getConstructor().newInstance();
            Handler annotation = clazz.getAnnotation(Handler.class);
            String path = server.getContextPath() + annotation.routePath();
            if (annotation.method().equals(RequestMethod.GET)){
                if(null != getHttpHandlerMap.get(path)){
                    log.error("处理器存在路径冲突，请检查:{}, {}", getHttpHandlerMap.get(path).getClass().getName(),
                            clazz.getName());
                }
                getHttpHandlerMap.put(path, httpHandler);
            } else if (annotation.method().equals(RequestMethod.POST)){
                if(null != postHttpHandlerMap.get(path)){
                    log.error("处理器存在路径冲突，请检查:{}, {}", postHttpHandlerMap.get(path).getClass().getName(),
                            clazz.getName());
                }
                postHttpHandlerMap.put(path, httpHandler);
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

    // TODO 这里的请求处理逻辑分开，需要分为两步
    //      - 解析请求
    //      - 处理请求
    // ToDo 这里尝试使用动态代理进行错误处理
    @Override
    public Void apply(Socket socket) {
        InputStream socketIn = null;
        OutputStream socketOut = null;
        HttpRequest request = null;
        String routePath = null;
        RequestMethod method = null;
        try {
            socketIn = socket.getInputStream();
            socketOut = socket.getOutputStream();
            request = this.requestParser.parse(socketIn);
            routePath = request.getUrlWrapper().getUrl().getPath();
            method = request.getBody() == null ? RequestMethod.GET : RequestMethod.POST;
            HttpHandler handler = route(method, routePath);
            HttpResponse response = handler.handle(request);
            // 处理 Response
            responseBuilder.buildAndWrite(socketOut, response);
        }catch (IOException e) {
            log.error("IO异常");
            if(socketOut != null) {
                try {
                    responseBuilder.failAndBuild(socketOut, new ServerSnapShotException(e
                            , routePath, method.name(), StatusCode.INTERNAL_SERVER_ERROR));
                } catch (IOException ex) {
                    log.error("客户端IO异常，连接可能已被客户端提前关闭");
                }
            }
        }catch(ServerSnapShotException e){
            try {
                responseBuilder.failAndBuild(socketOut, e);
            } catch (IOException ignored) {
                log.error("客户端IO异常，连接可能已被客户端提前关闭");
            }
        }catch (RuntimeException e){
            log.error("运行时异常,{}",e.getMessage());
            try {
                responseBuilder.failAndBuild(socketOut, new ServerSnapShotException(e
                        , routePath, method.name(), StatusCode.INTERNAL_SERVER_ERROR));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }finally {
            // Socket 资源清理
            cleanUp(socketIn, socketOut, socket);
        }
        return null;
    }

    private void cleanUp(Closeable... objects){
        for (Closeable o : objects){
            try {
                o.close();
            } catch (IOException ignored) {}
        }
    }

    public HttpHandler route(RequestMethod method, String routePath) throws ServerSnapShotException{
        HttpHandler handler = router.route(method, routePath);
        if(null == handler){
            log.error("请求路径错误，未知的请求路径: {}", routePath);
            throw new ServerSnapShotException(routePath, method.name(), StatusCode.NOT_FOUND);
        }
        return handler;
    }
}
