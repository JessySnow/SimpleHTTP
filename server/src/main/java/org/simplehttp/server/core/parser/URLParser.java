package org.simplehttp.server.core.parser;

import org.simplehttp.common.core.URLWrapper;
import org.simplehttp.server.core.SimpleHttpServer;

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
     * @param urlStr 完整的请求 URL 路径
     */
    public URLWrapper parse(SimpleHttpServer server, String urlStr) throws RuntimeException{
        try {
            URL url = new URL(urlStr);
            checkPath(server, url);
            return new URLWrapper(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad Url");
        }
    }

    private void checkPath(SimpleHttpServer server, URL url){
        if(!url.getPath().startsWith(server.getContextPath())){
            throw new RuntimeException("不支持的请求路径");
        }
    }
}
