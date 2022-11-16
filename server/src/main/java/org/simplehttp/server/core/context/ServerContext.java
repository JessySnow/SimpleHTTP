package org.simplehttp.server.core.context;

import org.simplehttp.server.core.session.Session;
import org.simplehttp.server.core.session.UserSession;
import org.simplehttp.server.core.session.impl.ContextUserSession;

import java.util.HashMap;

/**
 * 服务器上下文，提供 Session 管理、线程分派、文件缓存 的功能
 * 通过继承 Context 来自定义其他的功能，并绑定到 SimpleHttpServer 对象上
 * 默认组件:
 * 可选组件：
 *      @see ContextUserSession -- 用户 Session 服务
 */
public class ServerContext {
    // 只读 Map，在服务器启动时进行初始化，后续只会进行读操作从上下文拿 Session 实现功能，启动后不要向这个 SessionMap 中写内容
    private final HashMap<Class<? extends Session<?,?>>, Session<?,?>> sessionHashMap = new HashMap<>();

    // 下文支持的功能标志位
    private boolean isStateFul;
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

    public boolean isStateFul() {
        return isStateFul;
    }

    public void setStateFul(boolean stateFul) {
        isStateFul = stateFul;
    }

    public boolean isFileCached() {
        return isFileCached;
    }

    public void setFileCached(boolean fileCached) {
        isFileCached = fileCached;
    }

    public ServerContext(){
        this.isFileCached = false;
        this.isFileCached = false;
    }

    public ServerContext(Session<?,?> session){
        this();
        this.bindSession(session);
    }
}
