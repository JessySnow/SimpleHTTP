package org.simplehttp.server.core.parser;

import org.simplehttp.common.core.URLWrapper;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.AbstractComponent;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.exception.ServerSnapShotException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO support for restful
 * 请求路径解析(只支持 CGI 风格的 URL 解析)，请求进入后，由这个组件负责解析路径
 * eg: http://mail.google.com:80/context/api?mailid=10001&userid=1234
 */
public class URLParser extends AbstractComponent {

    public URLParser(BaseServerContext context){
        super(context);
    }

    /**
     * @param urlStr 完整的请求 URL 路径
     */
    public URLWrapper parse(String urlStr) throws ServerSnapShotException {
        try {
            URL url = new URL(urlStr);
            checkPath(context.server, url);
            return new URLWrapper(url);
        } catch (MalformedURLException e) {
            throw new ServerSnapShotException(e, urlStr, "UN_KNOWN", StatusCode.BAD_REQUEST);
        }
    }

    private void checkPath(SimpleHttpServer server, URL url) throws ServerSnapShotException{
        if(!url.getPath().startsWith(server.getContextPath())){
            throw new ServerSnapShotException(url.toString(), "UN_KNOWN", StatusCode.NOT_FOUND);
        }
    }
}
