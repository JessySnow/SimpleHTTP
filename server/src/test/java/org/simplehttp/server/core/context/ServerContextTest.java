package org.simplehttp.server.core.context;

import org.junit.jupiter.api.Test;
import org.simplehttp.server.core.session.UserSession;
import org.simplehttp.server.core.session.impl.ContextUserSession;

import static org.junit.jupiter.api.Assertions.*;

class ServerContextTest {

    // 绑定 Session 标志位测试
    @Test
    public void testSessionBind(){
        UserSession userSession = new ContextUserSession();
        ServerContext serverContext = new ServerContext();
        serverContext.bindSession(userSession);
        assertTrue(serverContext.isStateFul());
        assertFalse(serverContext.isFileCached());
        UserSession userSession1 = serverContext.getUserSession();
        assertNotNull(userSession1);
    }


}