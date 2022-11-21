package org.simplehttp.server.handler.impl;

import org.simplehttp.server.enums.MIME;
import org.simplehttp.server.enums.RequestMethod;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.handler.HttpHandler;
import org.simplehttp.server.handler.annonation.Handler;
import org.simplehttp.server.pojo.protocol.HttpBody;
import org.simplehttp.server.pojo.protocol.HttpHeader;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.HttpResponse;

/**
 * Handler 示例
 */
@Handler(method = RequestMethod.GET, routePath = "/echo")
public class EchoHandler implements HttpHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        HttpHeader header = response.getHeader();
        HttpBody body = response.getBody();

        // 必填，表示当前的请求是否成功，这里返回 200 OK
        header.setStatusCode(StatusCode.OK);
        // 必填，表示当前返回的媒体类型
        header.setContentType(MIME.TEXT_HTML);

        // 选填，向客户端返回的消息内容，如果没有可以不填
        // 返回实体部分的 POJO 由两个部分组成，媒体类型和实际的内容
        // 这里我返回 HTML 类型
        // 构造 HTML 文本
        String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>SimpleHttpServer!</title>
                        <style>
                            body {
                                width: 35em;
                                margin: 0 auto;
                                font-family: Tahoma, Verdana, Arial, sans-serif;
                                background-image:url('https://s1.ax1x.com/2022/11/20/zKz4KI.jpg');
                            }
                        </style>
                    </head>
                    <body>
                    <h1>欢迎来到 SimpleHttpServer!</h1>
                    <p>如果你看见了这个页面, SimpleHttpServer 已经成功工作.</p>
                    <p><em>感谢使用 SimpleHttpServer.</em></p>
                    </body>
                    </html>
                """;

        // 装填到 body 中，当前对于 HTML、PLAIN_TEXT 的处理是将键设置成 MIME.TEXT_PLAIN
        body.putValue(MIME.TEXT_HTML, html);

        return response;
    }
}
