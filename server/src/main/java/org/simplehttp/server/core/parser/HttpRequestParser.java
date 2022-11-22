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
import java.nio.charset.Charset;
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
    private static final byte COLON = ':';

    public HttpRequest parse(BaseServerContext context, InputStream inputStream) throws IOException, RuntimeException {
        HttpRequest request = new HttpRequest();
        HttpHeader header = new HttpHeader();
        HttpBody body = null;

        byte pre = -1;
        byte cur;

        // 预分配缓冲区，解析 HTTP 请求头，单个键/请求路径长度最大限制 512 字节
        ByteBuffer buffer = ByteBuffer.allocate(512);
        // 处理请求头部的首行
        String method = null;
        String queryPath = null;
        String protocolVersion = null;
        while ((cur = (byte) inputStream.read()) != NEWLINE){
            if(SPACE == cur){
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes, 0, bytes.length);
                if(method == null){
                    method = new String(bytes);
                }else if(queryPath == null){
                    queryPath = new String(bytes);
                }else{
                    protocolVersion = new String(bytes);
                }
                buffer.clear();
            }
            else {
                buffer.put(cur);
            }
        }
        buffer.clear();

        //  TODO Fix
        String protocol = context.getServer().protocol;
        String host = context.getServer().getHostAlias();
        int port = context.getServer().getPort();
        queryPath = protocol + "://" + host + ":" + port + queryPath;

        // 请求方法处理，如果遇到不支持的请求方法，快速失败
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

        // 处理头部
        String key = null, value;
        while ((cur = (byte) inputStream.read()) != -1){
            // 键值对的冒号分割
            if(cur == COLON){
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                key = new String(bytes);
                buffer.clear();
            } else if(cur == NEWLINE || cur == ENTER){
                // 普通的换行，pre = '/r'
                if(pre == ENTER){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    value = new String(bytes);
                    buffer.clear();
                    header.addHeaderPair(key, value);
                } else if(pre == NEWLINE){
                    // 头、体 分界处，连续的两个换行符，直接退出循环
                    break;
                }
            }else{
                buffer.put(cur);
            }
            pre = cur;
        }
        request.setHeader(header);
        buffer.clear();

        // 请求体处理
        if(body != null){
            // 如果不能解析头部的媒体类型，使用默认的二进制类型进行处理
            String mimeType = Optional
                    .ofNullable(request
                            .getHeader()
                            .getHeaderValue(FixedHttpHeader.CONTENT_TYPE.key))
                    .orElse(MIME.BINARY.value);
            String[] split = mimeType.split(";");
            Charset charset = StandardCharsets.UTF_8;
            if(split.length > 1){
                mimeType = split[0];
                charset = Charset.forName(split[1]);
            }

            MIME acceptableType;
            try {
                acceptableType = Enum.valueOf(MIME.class, mimeType.replace("/","_").toUpperCase().trim());
            }catch (RuntimeException e){
                throw new RuntimeException("无法接受的媒体类型");
            }

            switch (acceptableType){
                case TEXT_HTML, TEXT_PLAIN -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,charset));
                    // 纯文本的提交不要超过 8MB，超过这个大小，使用 Multipart 格式提交
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
