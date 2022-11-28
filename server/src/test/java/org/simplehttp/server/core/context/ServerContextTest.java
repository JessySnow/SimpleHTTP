package org.simplehttp.server.core.context;

import org.junit.jupiter.api.Test;
import org.simplehttp.server.core.session.UserSession;
import org.simplehttp.server.core.session.impl.ContextUserSession;

class ServerContextTest {

    // 绑定 Session 标志位测试
    @Test
    public void testSessionBind(){
        UserSession userSession = new ContextUserSession();
        ServerContext serverContext = new ServerContext();
    }


}