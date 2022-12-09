package org.simplehttp.handler.impl;

import org.simplehttp.common.enums.MIME;
import org.simplehttp.common.enums.RequestMethod;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.handler.annonation.Handler;
import org.simplehttp.server.pojo.protocol.HttpBody;
import org.simplehttp.server.pojo.protocol.HttpHeader;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;

@Handler(method = RequestMethod.GET, routePath = "/session_test")
public class SessionHandler extends HttpHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        String sessionKey = request.getUrlWrapper().getQueryValue("sessionKey");
        String sessionValue = request.getUrlWrapper().getQueryValue("sessionValue");

        HttpResponse response = new HttpResponse();
        HttpHeader header = response.getHeader();
        HttpBody body = response.getBody();

        StringBuilder sb = new StringBuilder();
        if(this.context.isInSession(sessionKey)){
            sb.append("In Session");
        }else{
            sb.append("Not in session");
            this.context.putToSession(sessionKey, sessionValue);
        }

        header.setStatusCode(StatusCode.OK);
        header.setContentType(MIME.TEXT_PLAIN);
        body.putValue(MIME.TEXT_PLAIN, sb.toString());
        return response;
    }
}
