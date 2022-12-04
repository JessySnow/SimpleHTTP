package org.simplehttp.server.enums.pojo.protocol;

import lombok.Getter;

/**
 * Http 响应实体，由 HttpResponseBuilder 帮助我们把 HTTP 响应写到输出流中
 * @see org.simplehttp.server.core.parser.HttpResponseBuilder
 *
 * 响应实体中有一个必填项，描述当前 HTTP 报文的重要属性，即响应码，这个属性填充到头部字段中即可
 * 如果向客户端回显消息或者向客户端传递文件，需要向 body 域中写入对应的文本消息或者文件句柄
 * @see org.simplehttp.server.handler.impl.EchoHandler
 */
@Getter
public class HttpResponse {
    private HttpHeader header;
    private HttpBody body;

    public HttpResponse(){
        this.header = new HttpHeader();
        this.body = new HttpBody();
    }
}
