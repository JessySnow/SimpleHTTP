package org.simplehttp.server.pojo.protocol;

import lombok.Data;
import org.simplehttp.server.enums.MIME;

import java.util.HashMap;

/**
 * Http 请求体、响应体实体
 * 由于 HTTP POST 请求会出现非 ASCII 数据，所以统一使用 Object 存储，通过标记内容类型的方式来还原内容
 */
public class HttpBody {
    private final HashMap<String, BodyValueEntry> body = new HashMap<>();

    public void addBodyValueEntry(String key, Object content, MIME mimeType){
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的键");
        }
        BodyValueEntry bodyValueEntry = new BodyValueEntry(content, mimeType);
        body.put(key, bodyValueEntry);
    }

    public void addBodyValueEntry(String key, BodyValueEntry entry){
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的键");
        }
        if(null == entry.getMimeType()){
            throw new IllegalArgumentException("不允许的媒体类型");
        }
        body.put(key, entry);
    }

    public BodyValueEntry getBodyValueEntry(String key){
        return body.get(key);
    }

    @Data
    public static class BodyValueEntry{
        private Object content;
        private MIME mimeType;

        public BodyValueEntry(){}

        BodyValueEntry(Object content, MIME mimeType){
            this.content = content;
            this.mimeType = mimeType;
        }
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
        return (String)this.body.get(MIME.TEXT_PLAIN.value).getContent();
    }
}
