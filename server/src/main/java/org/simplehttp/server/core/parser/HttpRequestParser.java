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
    private static final short MAX_HEADER_SIZE = 8192;

//    public HttpRequest parse(AbstractServerContext context, InputStream inputStream) throws IOException, RuntimeException {
//        HttpRequest request = new HttpRequest();
//        HttpHeader header = new HttpHeader();
//        HttpBody body = null;
//        // 对于 头部 和 Body 统一使用 UTF-8 编码进行读取
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//
//        String line;
//        int count = 0;
//        // 头部字段解析
//        while((line = reader.readLine()) != null && (!line.equals(""))){
//            if(0 == count){
//                String[] headerParams = line.split(" ");
//                String method = headerParams[0].toUpperCase();
//                String queryPath = headerParams[1];
//                // 协议字段不处理
//                String ignoredProtocol = headerParams[2].toUpperCase();
//
//                try{
//                    RequestMethod m = Enum.valueOf(RequestMethod.class, method);
//                    if(m.equals(RequestMethod.POST)){
//                        body = new HttpBody();
//                    }
//                }catch (RuntimeException e){
//                    throw new RuntimeException("不支持的 HTTP 请求方法");
//                }
//                URLWrapper urlWrapper = context.getUrlParser().parse(context.server, queryPath);
//                request.setUrlWrapper(urlWrapper);
//            }
//            else {
//                String[] entry = line.split(":");
//                // 错误的解析
//                if (entry.length < 2) {
//                    continue;
//                }
//                header.addHeaderPair(entry[0], entry[1]);
//            }
//            ++ count;
//        }
//        request.setHeader(header);
//
//        // POST 方法，需要解析请求体
//        if(body != null) {
//            // 如果不能解析头部的媒体类型，使用默认的二进制类型进行处理
//            String mimeType = Optional
//                    .ofNullable(request
//                            .getHeader()
//                                .getHeaderValue(FixedHttpHeader.CONTENT_TYPE.key))
//                    .orElse(MIME.BINARY.value);
//            int contentLength = Integer.parseInt(Optional
//                    .ofNullable(request
//                            .getHeader()
//                            .getHeaderValue(FixedHttpHeader.CONTENT_LENGTH.key))
//                    .orElse("-1"));
//            contentLength = contentLength == 0 ? -1 : contentLength;
//
//
//            MIME acceptableType = null;
//            try {
//                acceptableType = Enum.valueOf(MIME.class, mimeType.replace("/","_").toUpperCase());
//            }catch (RuntimeException e){
//                throw new RuntimeException("无法接受的媒体类型");
//            }
//
//            // 解析工作
//            switch (acceptableType){
//                case TEXT_PLAIN, TEXT_HTML -> {
//                    HttpBody.BodyValueEntry entry = new HttpBody.BodyValueEntry();
//                    entry.setMimeType(MIME.TEXT_PLAIN);
//
//                    // 如果请求头有包含了长度信息，则使用这个长度信息来初始化 content，否则需要使用 ByteArrayOutputStream 进行转储
//                    if(contentLength != -1){
//                        byte[] content = new byte[contentLength];
//                        inputStream.read(content);
//                        entry.setContent(content);
//                    }
//                    else {
//                        ByteArrayOutputStream cache = new ByteArrayOutputStream();
//                        byte byt;
//                        while ((byt = (byte) inputStream.read()) != -1) {
//                            cache.write(byt);
//                        }
//                        entry.setContent(cache.toByteArray());
//                    }
//                    body.addBodyValueEntry(MIME.TEXT_PLAIN.value, entry);
//                }
//            }
//        }
//        request.setBody(body);
//
//        return request;
//    }

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


        //TODO 处理 Body，由于使用了 UTF-8 编码不能直接使用 read 去读取，转换会溢出
//        if(body != null){
//            // 如果不能解析头部的媒体类型，使用默认的二进制类型进行处理
//            String mimeType = Optional
//                    .ofNullable(request
//                            .getHeader()
//                            .getHeaderValue(FixedHttpHeader.CONTENT_TYPE.key))
//                    .orElse(MIME.BINARY.value);
//            MIME acceptableType = null;
//            try {
//                acceptableType = Enum.valueOf(MIME.class, mimeType.replace("/","_").toUpperCase());
//            }catch (RuntimeException e){
//                throw new RuntimeException("无法接受的媒体类型");
//            }
//
//            switch (acceptableType){
//                // 纯文本信息，全部读取以 String 类型存放到 body 中，key 字段设为 plain/text，编码默认使用 UTF-8
//                case TEXT_HTML, TEXT_PLAIN -> {
//                    while ((cur = (byte) inputStream.read()) != -1){
//
//                    }
//                }
//            }
//        }
        request.setBody(body);

        return request;
    }
}
