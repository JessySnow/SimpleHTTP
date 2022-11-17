package org.simplehttp.server.enums;

/**
 * 支持的 HTTP 头信息
 */
public enum FixedHttpHeader {
    SERVER("Server"),
    SET_COOKIE("Set-Cookie"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_ENCODING("Content-Encoding");

    public final String key;

    FixedHttpHeader(String key){
        this.key = key;
    }
}
