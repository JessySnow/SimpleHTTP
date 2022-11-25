package org.simplehttp.server.exception;

import lombok.Data;
import org.simplehttp.common.exception.SnapShotException;
import org.simplehttp.server.enums.StatusCode;

@Data
public class ServerSnapShotException extends SnapShotException {
    public ServerSnapShotException(){
        super();
    }

    /**
     * 对当前的错误保存一份快照，这个异常会由框架代为处理
     * @param url 请求 url，或者部分的请求 url
     * @param method 请求方法
     * @param code 响应码
     */
    public ServerSnapShotException(String url, String method, StatusCode code){
        this.url = url;
        this.requestMethod = method;
        this.code = code;
    }

    /**
     * 报错信息不全的情况下尽量将原因传递给上层
     * @param cause 原始报错
     * @param url 请求 url，或者部分的请求 url
     * @param method 请求方法
     * @param code 响应码
     */
    public ServerSnapShotException(Exception cause, String url, String method, StatusCode code){
        this.url = url;
        this.requestMethod = method;
        this.code = code;
    }
    // 错误码
    protected StatusCode code;
}
