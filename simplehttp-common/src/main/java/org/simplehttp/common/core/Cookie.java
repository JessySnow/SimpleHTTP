package org.simplehttp.common.core;

import lombok.Getter;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Cookie 实体
 */
public class Cookie {

    public static final long YEAR_SEC =((long)365 * (long)86400 * (long)1000);

    // UUID
    public final String uuid;

    // Cookie 有效路径
    @Getter
    private String path;

    // 赏味期限
    @Getter
    @Setter
    private Date expiration;

    // Cookie 值
    @Getter
    private String value;

    // Cookie 键
    @Getter
    private String key;

    private Cookie(){
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * 默认 Cookie 构造函数
     * @param value Cookie 值
     * @param path 有效路径
     * @param key Cookie 键
     */
    public Cookie(String key, String value, String path){
        this();
        this.path = encodePath(path);
        this.key = encode(key);
        this.value = encode(value);
        this.expiration = new Date(System.currentTimeMillis() + 3600000);
    }

    /**
     * 带有过期时间的 Cookie 构造函数
     * 如果过期时间被设置为 0，这个 Cookie 会被当作会话 Cookie 处理(具体见客户端对 Session Cookie 的处理)
     * 如果过期时间被设置为一个负值，这个 Cookie 会被当作一个不会过期的 Cookie(365天的有效期)
     * @param value Cookie 值
     * @param path 有效路径
     * @param second 过期时间，以秒为单位
     * @param key Cookie 键
     */
    public Cookie(String key, String value, String path, int second){
        this();
        // 永久生效的 Cookie
        if (second < 0){
            this.expiration = new Date(System.currentTimeMillis() + YEAR_SEC);
        }else if(second > 0){
            this.expiration = new Date(System.currentTimeMillis() + second * 1000L);
        }else{
            this.expiration = null;
        }

        this.key = encode(key);
        this.value = encode(value);
        this.path = encodePath(path);
    }

    /**
     * 全局 Cookie，有效路径将会被设置为当前服务器的上下文路径，过期时间设置为 30 分钟
     * @param key Cookie 键
     * @param value Cookie 值
     */
    public Cookie(String key, String value){
        this(key, value, null);
    }

    public void setKey(String key) {
        this.key = encode(key);
    }

    public void setPath(String path) {
        this.path = encodePath(path);
    }

    public void setValue(String value) {
        this.value = encode(value);
    }

    private static String encode(String param){
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }
    private static String encodePath(String path){
        String[] paths = path.split("/");
        for(int i = 0; i < paths.length; ++ i){
            paths[i] = URLEncoder.encode(paths[i], StandardCharsets.UTF_8);
        }

        StringBuilder sb = new StringBuilder();
        for(String s : paths){
            if(!s.isEmpty()){
                sb.append("/").append(s);
            }
        }

        return sb.toString();
    }
}
