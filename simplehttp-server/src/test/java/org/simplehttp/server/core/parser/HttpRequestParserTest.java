package org.simplehttp.server.core.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.exception.ServerSnapShotException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class HttpRequestParserTest {
    private BaseServerContext context;
    private ByteArrayInputStream getHeaderStream;
    private ByteArrayInputStream postHeaderStream_plain_text;


    @BeforeEach
    public void setUp(){
        this.context = new BaseServerContext();
        context.server = new SimpleHttpServer().bindContext(context)
                .setPort(9090)
                .setContextPath("/api")
                .setHostAlias("localhost");
        getHeaderStream = new ByteArrayInputStream((
                "GET /api?id=1 HTTP/1.1\n" +
                        "User-Agent:Fiddler Everywhere\n" +
                        "Host:127.0.0.1:7890\r\n")
                .getBytes());
        postHeaderStream_plain_text = new ByteArrayInputStream(("POST /api/index.jsp HTTP/1.1\n" +
                "User-Agent:Fiddler Everywhere\r\n" +
                "Host:127.0.0.1:7890\r\n" +
                "Content-Type:text/plain\r\n" +
                "Content-Length:0\r\n" +
                "\n" +
                "This is a plain text test.").getBytes());
    }

    @Test
    public void testParseGet() throws IOException, ServerSnapShotException {
        HttpRequest request = new HttpRequestParser(context).parse(getHeaderStream);
    }

    /**
     * POST 请求测试
     * 媒体类型：纯文本
     */
    @Test
    public void testParsePost() throws IOException, ServerSnapShotException {
        HttpRequest request = new HttpRequestParser(context).parse(postHeaderStream_plain_text);
    }
}