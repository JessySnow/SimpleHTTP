package org.simplehttp.server.core.parser;

import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.enums.FixedHttpHeader;
import org.simplehttp.server.enums.MIME;
import org.simplehttp.server.pojo.protocol.HttpBody;
import org.simplehttp.server.pojo.protocol.HttpResponse;

import java.io.OptionalDataException;
import java.io.OutputStream;
import java.util.Optional;

public class HttpResponseBuilder {

    // 构建请求并将请求写入到 Socket 中
    public static void buildAndWrite(OutputStream outputStream, HttpResponse response){
        String protocol = "HTTP/1.0";
        String server = SimpleHttpServer.Server;
        String contentType = response.getHeader().getContentType();
        String statusCode = response.getHeader().getStatusCode();
        MIME acceptableType = Enum.valueOf(MIME.class, contentType.replace("/", "_")
                                                                                        .toUpperCase()
                                                                                        .trim());

        StringBuilder responseBuilder = new StringBuilder();
        // 首行
        responseBuilder.append(protocol).append(" ").append(statusCode).append("\r\n");
        // 属性行
        responseBuilder.append(FixedHttpHeader.SERVER).append(":").append(server).append("\r\n");
        responseBuilder.append(FixedHttpHeader.CONTENT_TYPE).append(":").append(contentType).append("\r\n");
        // 分隔符
        responseBuilder.append("\n");

        // 处理响应体
        switch (acceptableType){

        }
    }
}
