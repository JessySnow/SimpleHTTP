package org.simplehttp.server.pojo.protocol;

import lombok.Data;
import org.simplehttp.server.enums.MIME;

import java.util.HashMap;

/**
 * Http 请求体、响应体实体
 * 由于 HTTP multipart POST 请求会出现非 ASCII 数据，所以统一使用 byte 数组存储，通过标记内容类型的方式来还原内容
 */
public class HttpBody {
    private final HashMap<String, BodyValueEntry> body = new HashMap<>();

    public void addBodyValueEntry(String key, byte[] content, MIME mimeType){
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的");
        }
        BodyValueEntry bodyValueEntry = new BodyValueEntry(content, mimeType);
        body.put(key, bodyValueEntry);
    }

    public void addBodyValueEntry(String key, BodyValueEntry entry){
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的");
        }
        if(null == entry.getMimeType()){
            throw new IllegalArgumentException("不允许的");
        }
        body.put(key, entry);
    }

    public BodyValueEntry getBodyValueEntry(String key){
        return body.get(key);
    }

    @Data
    public static class BodyValueEntry{
        private byte[] content;
        private MIME mimeType;

        BodyValueEntry(){}

        BodyValueEntry(byte[] content, MIME mimeType){
            this.content = content;
            this.mimeType = mimeType;
        }
    }
}
