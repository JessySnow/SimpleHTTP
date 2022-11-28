package org.simplehttp.server.core.context;

import org.simplehttp.server.core.session.impl.ContextUserSession;

/**
 * 支持状态管理的上下文，这个上下文支持了 HttpSession
 * Session 容器选择使用并发map
 */
public class ServerContext extends BaseServerContext {
    private ContextUserSession userSession = new ContextUserSession();


}
