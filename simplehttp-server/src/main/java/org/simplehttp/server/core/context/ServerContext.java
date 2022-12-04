package org.simplehttp.server.core.context;

import org.simplehttp.server.core.session.impl.ContextUserSession;

/**
 * 支持 Http Session 的服务器上下文
 * 目前仅支持基于 Cookie 的 Session
 */
public class ServerContext extends BaseServerContext {
    private ContextUserSession userSession = new ContextUserSession();
}
