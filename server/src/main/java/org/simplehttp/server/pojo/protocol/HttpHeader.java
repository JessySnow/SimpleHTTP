package org.simplehttp.server.pojo.protocol;

import lombok.Data;
import org.simplehttp.server.enums.FixedHttpHeader;
import org.simplehttp.server.enums.MIME;
import org.simplehttp.server.enums.StatusCode;

import java.util.HashMap;

/**
 * 头部实体
 * 由于 HTTP 头部只支持扩展 ASCII 范围内的字符，所以使用String -- String 键值对
 */
public class HttpHeader {
    private final HashMap<String, String> header = new HashMap<>();

    /**
     * 添加头部键值对，为了避免出现 value 中有 '\r' '\n' 导致的协议解析失败，默认不允许添加这两个字符到头部中
     * @param key 键
     * @param value 值
     */
    public void addHeaderPair(String key, String value) throws IllegalArgumentException{
        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("不允许的请求头键");
        }
        if(value.contains("\r") || value.contains("\n")){
            throw new IllegalArgumentException("不允许请求头出现分隔符");
        }
        header.put(key, value);
    }

    public String getHeaderValue(String key){
        return header.get(key);
    }

    // 常见属性暴露的方法，如果没有设置这个属性，返回该属性的默认值
    // 媒体类型
    public HttpHeader setContentType(String type){
        this.header.put(FixedHttpHeader.CONTENT_TYPE.key, type);
        return this;
    }
    public String getContentType(){
        String type;
        return (type = this.header.get(FixedHttpHeader.CONTENT_TYPE.key)) == null ? MIME.BINARY.value : type;
    }
    // 响应状态
    public HttpHeader setStatusCode(StatusCode statusCode){
        this.header.put(FixedHttpHeader.STATUS_CODE.key, statusCode.getCode() + " " + statusCode.getStatus());
        return this;
    }
    public String getStatusCode(){
        return this.header.get(FixedHttpHeader.STATUS_CODE.key);
    }


    @Override
    public String toString() {
        return "HttpHeader{" +
                "header=" + header +
                '}';
    }
}
