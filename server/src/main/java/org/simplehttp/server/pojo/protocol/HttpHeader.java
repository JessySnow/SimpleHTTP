package org.simplehttp.server.pojo.protocol;

import lombok.Data;

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

    @Override
    public String toString() {
        return "HttpHeader{" +
                "header=" + header +
                '}';
    }
}
