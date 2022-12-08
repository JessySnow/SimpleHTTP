package org.simplehttp.server.core.context;

import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;
import org.simplehttp.server.exception.ServerSnapShotException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上下文行为接口
 */
public interface ContextInterface {
    HttpRequest parse(InputStream socketIn) throws IOException, ServerSnapShotException;

    HttpResponse invoke(HttpRequest request) throws IOException, ServerSnapShotException;

    default HttpResponse response(HttpResponse httpResponse)
            throws IOException, ServerSnapShotException{
        return httpResponse;
    }
}
