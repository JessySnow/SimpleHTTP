package org.simplehttp.server.enums.pojo.protocol;

import org.simplehttp.common.enums.MIME;

import java.util.HashMap;

/**
 * Http 请求体、响应体实体
 * 由于 HTTP POST 请求会出现非 ASCII 数据，所以统一使用 Object 存储，通过标记内容类型的方式来还原内容
 * 静态内部类 BodyValueEntry 使用 MIME 类型作为键，Object 存放实际的内容
 */
public class HttpBody {
    private final HashMap<MIME, Object> body = new HashMap<>();

    public void putValue(MIME type, Object content){
        if(type == null){
            throw new IllegalArgumentException("不允许的键");
        }
        body.put(type, content);
    }

    @Override
    public String toString() {
        return "HttpBody{" +
                "body=" + body +
                '}';
    }

    // 对于常见的几种返回类型添加的支持方法
    // 对于返回类型是普通文本的情况
    public String getText(){
        String res;
        return (res = (String)this.body.get(MIME.TEXT_PLAIN)) == null ?
                (String) this.body.get(MIME.TEXT_HTML) :
                res;
    }
}
