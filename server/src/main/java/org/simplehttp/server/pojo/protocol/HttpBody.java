package org.simplehttp.server.pojo.protocol;

import org.simplehttp.server.enums.MIME;

import java.util.HashMap;

/**
 * Http 请求体、响应体实体
 * 由于 HTTP multipart POST 请求会出现非 ASCII 数据，所以统一使用 byte 数组存储，通过标记内容类型的方式来还原内容
 */
public class HttpBody {
    private final HashMap<String, BodyValueEntry> body = new HashMap<>();

    public void addBodyPair(String key, byte[] content, MIME mimeType){
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的请求体键");
        }
        BodyValueEntry bodyValueEntry = new BodyValueEntry(content, mimeType);
        body.put(key, bodyValueEntry);
    }

    public void addBodyPair(String key, BodyValueEntry entry){
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的请求体键");
        }
        body.put(key, entry);
    }

    public static class BodyValueEntry{
        private byte[] content;
        private MIME mimeType;

        BodyValueEntry(){}

        BodyValueEntry(byte[] content, MIME mimeType){
            this.content = content;
            this.mimeType = mimeType;
        }

        public byte[] getContent() {
            return content;
        }


        public MIME getMimeType() {
            return mimeType;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public void setMimeType(MIME mimeType) {
            this.mimeType = mimeType;
        }
    }
}
