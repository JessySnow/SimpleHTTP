package org.simplehttp.server.core.parser;

import lombok.extern.log4j.Log4j2;
import org.simplehttp.server.core.SimpleHttpServer;
import org.simplehttp.common.enums.FixedHttpHeader;
import org.simplehttp.common.enums.MIME;
import org.simplehttp.server.enums.StatusCode;
import org.simplehttp.server.enums.pojo.protocol.HttpResponse;
import org.simplehttp.server.exception.ServerSnapShotException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Log4j2
public class HttpResponseBuilder {

    // 构建请求并将请求写入到 Socket 中
    public void buildAndWrite(OutputStream outputStream, HttpResponse response) throws IOException{
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
            contentType = MIME.BINARY.value;
        }

        // 文本信息添加编码类型
        if(contentType.contains("text")){
            contentType += " ;charset=utf-8";
        }

        // 首行
        String head = protocol + " " + statusCode + "\r\n" +
                // 属性行
                FixedHttpHeader.SERVER.key + ":" + server + "\r\n" +
                FixedHttpHeader.CONTENT_TYPE.key + ":" + contentType + "\r\n";

        String body;
        // 处理响应体
        switch (acceptableType){
            // 文本类型
            case TEXT_HTML, TEXT_PLAIN -> {
                body = response.getBody().getText();
                String content = head + "\n" + body;
                byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                outputStream.write(bytes);
            }
        }
    }

    // 响应失败的请求
    public void failAndBuild(OutputStream outputStream, ServerSnapShotException exception) throws IOException {
        StatusCode code = exception.getCode();
        String url = exception.getUrl();
        String requestMethod = exception.getRequestMethod();
        log.error("请求处理失败\n\t请求URL/PATH: {}\n\t请求方法: {}\n\t响应: {}, {}", url,
                                                                                        requestMethod,
                                                                                        code.getCode(),
                                                                                        code.getStatus());
        String res = "HTTP/1.0 " + code.getCode() + " " + code.getStatus() + " \r\n"
                + "Server: SimpleHttp+\r\n"
                + "Content_Type: text/html\r\n\n"
                +"<html> "
                + "<head><title>" + code.getCode() + code.getStatus() +  "</title></head>"
                + "<body><center><h1>" + code.getCode() + " " + code.getStatus() + "</h1></center></body>"
                + "</html><hr><center>SimpleHttp</center>";
        outputStream.write(res.getBytes());
    }
}
