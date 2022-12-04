package org.simplehttp.server.core.route;

import org.simplehttp.common.enums.RequestMethod;
import org.simplehttp.server.core.context.AbstractComponent;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.handler.HttpHandler;

/**
 * CGI 风格服务器路由
 */
public class CGIRouter extends AbstractComponent implements Router {
    public CGIRouter(BaseServerContext context) {
        super(context);
    }

    @Override
    public HttpHandler route(RequestMethod method, String routePath) {
        if(method == RequestMethod.GET){
            return getContext().getGetHttpHandlerMap().get(routePath);
        }
        return getContext().getPostHttpHandlerMap().get(routePath);
    }
}
