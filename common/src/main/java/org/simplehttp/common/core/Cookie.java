package org.simplehttp.common.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * Cookie 实体
 */
public class Cookie {

    // UUID
    public final String uuid;

    // Cookie 有效路径
    @Getter
    @Setter
    private String path;

    // 赏味期限
    @Getter
    @Setter
    private Date expiration;

    // Cookie 值
    @Getter
    @Setter
    private String value;

    private Cookie(){
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * 默认 Cookie 构造函数
     * @param value Cookie 值
     * @param path 有效路径
     */
    public Cookie(String value, String path){
        this();
        this.path = path;
        this.value = value;
        this.expiration = new Date(System.currentTimeMillis() + 3600000);
    }

    /**
     * 带有过期时间的 Cookie 构造函数
     * @param value Cookie 值
     * @param path 有效路径
     * @param second 生效时间，以秒为单位
     */
    public Cookie(String value, String path, int second){
        this();
        // 永久生效的 Cookie
        if (second < 0){
            this.expiration = null;
        }else {
            this.value = value;
            this.path = path;
            this.expiration = new Date(System.currentTimeMillis() + second * 1000L);
        }
    }

}
