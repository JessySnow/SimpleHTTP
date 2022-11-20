package org.simplehttp.server.core.parser;

import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.server.enums.FixedHttpHeader;
import org.simplehttp.server.enums.MIME;
import org.simplehttp.server.pojo.protocol.HttpBody;
import org.simplehttp.server.pojo.protocol.HttpResponse;

import java.io.IOException;
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

        // 响应字段额外处理，如果返回类型是一个不支持的媒体类型，使用二进制让客户端直接下载
        MIME acceptableType;
        try{
            acceptableType = Enum.valueOf(MIME.class, contentType.replace("/", "_")
                                                                                        .toUpperCase()
                                                                                        .trim());
        } catch (Exception e){
            acceptableType = MIME.BINARY;
        }

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
            // 文本类型
            case TEXT_HTML, TEXT_PLAIN -> {

            }
        }

        byte[] res = responseBuilder.toString().getBytes();
        try {
            outputStream.write(res);
        } catch (IOException ignored) {
            // TODO 这个异常需要被处理
        }
    }
}
