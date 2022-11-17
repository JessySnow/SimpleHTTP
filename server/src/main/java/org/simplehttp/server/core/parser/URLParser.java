package org.simplehttp.server.core.parser;

import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.pojo.protocol.URLWrapper;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO support for restful
 * 请求路径解析(只支持 CGI 风格的 URL 解析)，请求进入后，由这个组件负责解析路径
 * eg: http://mail.google.com:80/context/api?mailid=10001&userid=1234
 */
public class URLParser {
    /**
     * @param server 服务器引用，构造 URL
     * @param urlQuery 完整的请求 URL 路径 eg: /context/api_1?user_id=1
     */
    public URLWrapper parse(SimpleHttpServer server, String urlQuery) throws RuntimeException{
        checkURLPath(server, urlQuery);
        String fullUrl = server.protocol + "://" + server.getHostAlias() + ":" + server.getPort() + urlQuery;
        try {
            return new URLWrapper(new URL(fullUrl));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad Url");
        }
    }

    private void checkURLPath(SimpleHttpServer server, String urlPath) throws RuntimeException{
        if(!urlPath.startsWith(server.getContextPath())){
            throw new RuntimeException("未知路径");
        }
    }
}
