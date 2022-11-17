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
        context.server = new SimpleHttpServer().setServerContext(context)
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

    @Test
    public void testParsePost() throws IOException{
        HttpRequest request = new HttpRequestParser().parse(context, postHeaderStream_plain_text);
    }

    /**
     * ASCII Reader 测试
     * @throws IOException
     */
    @Test
    public void testReaderFormat() throws IOException {
        byte[] bytes = "你好，世界！".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream cache = new ByteArrayOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null){
            cache.write(line.getBytes());
        }

        byte[] bs = cache.toByteArray();
        assertArrayEquals(bs, bytes);
    }
}