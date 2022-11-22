package org.simplehttp.common.enums;

/**
 * 支持的 HTTP 头信息
 */
public enum FixedHttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_ENCODING("Content-Encoding"),
    // 响应头独有
    SET_COOKIE("Set-Cookie"),
    SERVER("Server"),
    STATUS_CODE("Status Code");

    public final String key;

    FixedHttpHeader(String key){
        this.key = key;
    }
}
