package org.simplehttp.server.core.parser;

import org.simplehttp.server.core.context.AbstractServerContext;
import org.simplehttp.server.enums.FixedHttpHeader;
import org.simplehttp.server.enums.MIME;
import org.simplehttp.server.enums.RequestMethod;
import org.simplehttp.server.pojo.protocol.HttpBody;
import org.simplehttp.server.pojo.protocol.HttpHeader;
import org.simplehttp.server.pojo.protocol.HttpRequest;
import org.simplehttp.server.pojo.protocol.URLWrapper;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpRequestParser {
    private static final byte NEWLINE = '\n';
    private static final byte ENTER = '\r';

    public HttpRequest parse(AbstractServerContext context, InputStream inputStream) throws IOException, RuntimeException {
        HttpRequest request = new HttpRequest();
        HttpHeader header = new HttpHeader();
        HttpBody body = null;

        // 头部扩展 ASCII 码处理, 服务器最大头部限制 8k
        byte pre = -1;
        byte cur;
        // 头部构造器
        StringBuilder temp = new StringBuilder();

        // 处理首行
        while ((cur = (byte) inputStream.read()) != NEWLINE){
            temp.append((char) cur);
        }
        String firstLineOfHeader = temp.toString();
        String[] headerParams = firstLineOfHeader.split("\s");
        String method = headerParams[0].toUpperCase();
        String queryPath = headerParams[1];
        // 协议字段不处理，仅支持 HTTP1.0 短连接
        String ignoredProtocol = headerParams[2].toUpperCase();
        try{
            RequestMethod m = Enum.valueOf(RequestMethod.class, method);
            // 当是一个 POST 请求时，才构造 HTTP 请求体对象
            if(m.equals(RequestMethod.POST)){
                body = new HttpBody();
            }
        }catch (RuntimeException e){
            throw new RuntimeException("不支持的 HTTP 请求方法");
        }
        URLWrapper urlWrapper = context.getUrlParser().parse(context.server, queryPath);
        request.setUrlWrapper(urlWrapper);
        temp.delete(0,temp.length());

        // 处理头部
        while ((cur = (byte) inputStream.read()) != -1){
            if(cur == NEWLINE || cur == ENTER){
                // 普通的换行
                if(pre != NEWLINE && pre != ENTER){
                    String line = temp.toString();
                    String[] param = line.split(":");
                    if(param.length < 2){
                        throw new RuntimeException("损坏的 HTTP 头部");
                    }
                    header.addHeaderPair(param[0], param[1]);
                    temp.delete(0, temp.length());
                }
                // 到达了分界处，跳出循环
                else{
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
            MIME acceptableType = null;
            try {
                acceptableType = Enum.valueOf(MIME.class, mimeType.replace("/","_").toUpperCase());
            }catch (RuntimeException e){
                throw new RuntimeException("无法接受的媒体类型");
            }

            // 字符类型统一用 UTF-8 编码读取 TODO 支持其他的编码方式
            switch (acceptableType){
                // 如果提交的是一个纯文本信息，说明在协议层面不存在键值对关系，直接将文本读取到 String 中，BodyEntry 的键设置成媒体类型
                // 如果需要文本信息中的键值对关系，需要在 Handler 中自己进行处理
                case TEXT_HTML, TEXT_PLAIN -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null){
                        temp.append(line);
                    }
                    body.addBodyValueEntry(acceptableType.value, temp.toString(), MIME.TEXT_PLAIN);
                }
            }
        }
        request.setBody(body);

        return request;
    }
}
