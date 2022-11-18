package org.simplehttp.server.core.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.ServerContext;
import org.simplehttp.server.pojo.protocol.HttpRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestParserTest {
    private ServerContext context;
    private ByteArrayInputStream getHeaderStream;
    private ByteArrayInputStream postHeaderStream_plain_text;


    @BeforeEach
    public void setUp(){
        this.context = new ServerContext();
        context.setUrlParser(new URLParser());
        context.server = new SimpleHttpServer().bindContext(context)
                .setPort(9090)
                .setContextPath("/api")
                .setHostAlias("localhost");
        getHeaderStream = new ByteArrayInputStream((
                "GET http://127.0.0.1:7890/api?id=1 HTTP/1.1\n" +
                        "User-Agent:Fiddler Everywhere\n" +
                        "Host:127.0.0.1:7890\r\n")
                .getBytes());
        postHeaderStream_plain_text = new ByteArrayInputStream(("POST http://127.0.0.1:7890/api/index.jsp HTTP/1.1\n" +
                "User-Agent:Fiddler Everywhere\n" +
                "Host:127.0.0.1:7890\n" +
                "Content-Type:text/plain\n" +
                "Content-Length:0\n" +
                "\n" +
                "This is a plain text test.").getBytes());
    }

    @Test
    public void testParseGet() throws IOException {
        HttpRequest request = new HttpRequestParser().parse(context, getHeaderStream);
    }

    /**
     * POST 请求测试
     * 媒体类型：纯文本
     */
    @Test
    public void testParsePost() throws IOException{
        HttpRequest request = new HttpRequestParser().parse(context, postHeaderStream_plain_text);
    }
}