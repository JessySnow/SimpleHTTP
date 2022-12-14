package org.simplehttp.handler.impl;

import org.simplehttp.common.enums.MIME;
import org.simplehttp.common.enums.RequestMethod;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.handler.annonation.Handler;
import org.simplehttp.server.enums.pojo.protocol.HttpBody;
import org.simplehttp.server.enums.pojo.protocol.HttpHeader;
import org.simplehttp.server.enums.pojo.protocol.HttpRequest;
import org.simplehttp.server.enums.pojo.protocol.HttpResponse;

/**
 * 路径参数处理器示例
 */
@Handler(method = RequestMethod.GET, routePath = "/param_test")
public class PathParamHandler implements HttpHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        String userName = request.getUrlWrapper().getQueryValue("userName");
        String plainText = "Hello " + userName;

        HttpResponse response = new HttpResponse();
        HttpHeader header = response.getHeader();
        HttpBody body = response.getBody();

        header.setStatusCode(StatusCode.OK);
        header.setContentType(MIME.TEXT_PLAIN);
        body.putValue(MIME.TEXT_PLAIN, plainText);


        return response;
    }
}
