package org.simplehttp.server.core.parser;

import lombok.extern.log4j.Log4j2;
import org.simplehttp.server.core.context.BaseServerContext;
import org.simplehttp.server.enums.FixedHttpHeader;
import org.simplehttp.server.enums.MIME;
import org.simplehttp.server.enums.RequestMethod;
import org.simplehttp.server.pojo.protocol.HttpBody;
import org.simplehttp.server.pojo.protocol.HttpHeader;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.URLWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 解析 Socket 流
 */
@Log4j2
public class HttpRequestParser {
    private static final byte NEWLINE = '\n';
    private static final byte ENTER = '\r';
    private static final byte SPACE = ' ';

    public HttpRequest parse(BaseServerContext context, InputStream inputStream) throws IOException, RuntimeException {
        HttpRequest request = new HttpRequest();
        HttpHeader header = new HttpHeader();
        HttpBody body = null;

        byte pre = -1;
        byte cur;

        // 预分配缓冲区，解析 HTTP 请求头
        ByteBuffer buffer = ByteBuffer.allocate(512);
        StringBuilder temp = new StringBuilder();
        // 处理首行
        String method = null;
        String queryPath = null;
        while ((cur = (byte) inputStream.read()) != NEWLINE){
            if(SPACE == cur){
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes, 0, bytes.length);
                if(method == null){
                    method = new String(bytes);
                }else if(queryPath == null){
                    queryPath = new String(bytes);
                }
                buffer.clear();
            }
            else {
                buffer.put(cur);
            }
        }

        //兼容单元测试和API测试 TODO release 移除
        if(!queryPath.startsWith("http")){
            String protocol = context.getServer().protocol;
            String host = context.getServer().getHostAlias();
            String contextPath = context.getServer().getContextPath();
            int port = context.getServer().getPort();
            queryPath = protocol + "://" + host + ":" + port + queryPath;
        }

        try{
            RequestMethod m = Enum.valueOf(RequestMethod.class, method);
            // 当是一个 POST 请求时，才构造 HTTP 请求体对象
            if(m.equals(RequestMethod.POST)){
                body = new HttpBody();
            }
        }catch (RuntimeException e){
            log.info("不支持的 HTTP 请求方法: " + method);
            throw new RuntimeException("不支持的 HTTP 请求方法: " + method);
        }
        URLWrapper urlWrapper = context.getUrlParser().parse(context.server, queryPath);
        request.setUrlWrapper(urlWrapper);
        temp.delete(0,temp.length());

        // 处理头部
        while ((cur = (byte) inputStream.read()) != -1){
            if(cur == NEWLINE || cur == ENTER){
                // 普通的换行，pre = '/r'
                if(pre == ENTER){
                    String line = temp.toString();
                    String[] param = line.split(":");
                    if(param.length < 2){
                        throw new RuntimeException("损坏的 HTTP 头部");
                    }
                    header.addHeaderPair(param[0], param[1]);
                    temp.delete(0, temp.length());
                }
                // 到达了分界处，跳出循环
                else if(pre == NEWLINE){
                    break;
                }
            }else{
                temp.append((char) cur);
            }
            pre = cur;
        }
        request.setHeader(header);
        temp.delete(0,temp.length());



        if(body != null){
            // 如果不能解析头部的媒体类型，使用默认的二进制类型进行处理
            String mimeType = Optional
                    .ofNullable(request
                            .getHeader()
                            .getHeaderValue(FixedHttpHeader.CONTENT_TYPE.key))
                    .orElse(MIME.BINARY.value);
            MIME acceptableType;
            try {
                acceptableType = Enum.valueOf(MIME.class, mimeType.replace("/","_").toUpperCase().trim());
            }catch (RuntimeException e){
                throw new RuntimeException("无法接受的媒体类型");
            }

            // 字符类型统一用 UTF-8 编码读取 TODO 支持其他的编码方式
            switch (acceptableType){
                // 如果提交的是一个纯文本信息，说明在协议层面不存在键值对关系，直接将文本读取到 String 中，BodyEntry 的键设置成媒体类型
                // 如果需要文本信息中的键值对关系，需要在 Handler 中自己进行处理
                case TEXT_HTML, TEXT_PLAIN -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));
                    // 如果是 POST 请求，分配的 Body 空间是 8MB
                    CharBuffer charBuffer = CharBuffer.allocate(1024*8192);
                    reader.read(charBuffer);
                    charBuffer.flip();
                    body.putValue(acceptableType, charBuffer.toString());
                }
            }
        }
        request.setBody(body);

        return request;
    }
}
