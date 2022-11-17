package org.simplehttp.server.core.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.core.context.ServerContext;
import org.simplehttp.server.pojo.protocol.HttpRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestParserTest {
    private ServerContext context;
    private ByteArrayInputStream getHeaderStream;


    @BeforeEach
    public void setUp(){
        this.context = new ServerContext();
        context.setUrlParser(new URLParser());
        context.server = new SimpleHttpServer().setServerContext(context)
                .setPort(9090)
                .setContextPath("/api")
                .setHostAlias("localhost");
        getHeaderStream = new ByteArrayInputStream((
                "GET http://127.0.0.1:7890/api?id=1 HTTP/1.1\n" +
                        "User-Agent:Fiddler Everywhere\n" +
                        "Host:127.0.0.1:7890\r\n")
                .getBytes());
    }

    @Test
    public void testParse() throws IOException {
        HttpRequest parse = new HttpRequestParser().parse(context, getHeaderStream);
        System.out.println("Success");
    }
}