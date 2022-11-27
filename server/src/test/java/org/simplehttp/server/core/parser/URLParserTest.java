package org.simplehttp.server.core.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.simplehttp.common.core.URLWrapper;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.exception.ServerSnapShotException;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class URLParserTest {
    SimpleHttpServer simpleHttpServer = new SimpleHttpServer();
    URL testUrl;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        this.simpleHttpServer.setHostAlias("testhost");
        this.simpleHttpServer.setContextPath("/api");
        this.simpleHttpServer.setPort(8090);
        testUrl = new URL("http://mail.google.com:80/context/index.jsp?mailid=10001&userid=1234");
    }

    @Test
    public void testURL() throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/context/api?id=1");
        System.out.printf("Protocol: %s\n", url.getProtocol());
        System.out.printf("Host: %s\n", url.getHost());
        System.out.printf("Path: %s\n", url.getPath());
        System.out.printf("Port: %s\n", url.getPort());
        System.out.printf("Query: %s\n", url.getQuery());
        System.out.flush();
    }

    @Test
    public void testParseBadContext(){
        assertThrows(ServerSnapShotException.class, () -> new URLParser().parse(simpleHttpServer, "http://127.0.0.1:8080/context/api?id=1"));
    }

    @Test
    public void testParseURL() throws ServerSnapShotException {
        simpleHttpServer.setContextPath("/context");
        URLWrapper parse = new URLParser().parse(simpleHttpServer, "http://127.0.0.1:8080/context/api?id=1");
        assertNotNull(parse);
    }

}