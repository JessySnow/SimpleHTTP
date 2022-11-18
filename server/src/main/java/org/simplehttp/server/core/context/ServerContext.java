package org.simplehttp.server.core.context;

import lombok.Getter;
import org.simplehttp.server.core.session.Session;
import org.simplehttp.server.core.session.UserSession;

/**
 * 支持状态管理的上下文，这个上下文支持了 HttpSession
 */
public class ServerContext extends BaseServerContext {
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
