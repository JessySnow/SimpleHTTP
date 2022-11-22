package org.simplehttp.server.enums.pojo.protocol;

import lombok.Data;
import org.simplehttp.common.core.URLWrapper;


/**
 * 最终需要处理的请求实体
 * @see HttpHeader 头部信息对象，头部的一些键值对信息就在这里面
 * @see HttpBody 请求体对象，请求体里面的文本、多媒体、文件在里面，以键值对的形式存放
 * @see URLWrapper 请求URL对象，里面比较重要的是路径参数这一个，其他的应该你不需要关心
 */
@Data
public class HttpRequest {
    private HttpHeader header;
    private HttpBody body;
    private URLWrapper urlWrapper;
}
