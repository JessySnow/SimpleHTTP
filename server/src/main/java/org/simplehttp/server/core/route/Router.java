package org.simplehttp.server.core.route;

import org.simplehttp.common.enums.RequestMethod;
import org.simplehttp.server.handler.HttpHandler;

public interface Router {
    HttpHandler route(RequestMethod method, String routePath);
}
