package org.simplehttp.server.core.context;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.simplehttp.server.core.parser.URLParser;
import org.simplehttp.server.core.session.Session;
import org.simplehttp.server.core.session.UserSession;
import org.simplehttp.server.core.session.impl.ContextUserSession;

import java.util.HashMap;

/**
 * 服务器上下文，提供 Session 管理、线程分派、文件存取 的功能
 * 通过继承 Context 来自定义其他的功能，并绑定到 SimpleHttpServer 对象上
 * 默认组件:
 * 可选组件：
 *      @see ContextUserSession -- 用户 Session 服务
 */
public class ServerContext extends AbstractServerContext{
    // 下文支持的功能标志位
    @Getter
    private boolean isStateFul;
    @Getter
    private boolean isFileCached;

    // 绑定 Session，并设置上下文支持的功能标志位，重复添加之后添加的会被忽略
    public ServerContext bindSession(Session<?,?> session){
        Class<? extends Session> aClass = session.getClass();
        if(UserSession.class.isAssignableFrom(aClass) && !isStateFul){
            this.isStateFul = true;
            sessionHashMap.put(UserSession.class, session);
        }
        return this;
    }

    // 从上下文获取到 UserSession
    public UserSession getUserSession(){
        if(isStateFul){
            return (UserSession)sessionHashMap.get(UserSession.class);
        }
        return null;
    }

    public ServerContext(){
        super();
    }

    public ServerContext(Session<?,?> session){
        this();
        this.bindSession(session);
    }
}
