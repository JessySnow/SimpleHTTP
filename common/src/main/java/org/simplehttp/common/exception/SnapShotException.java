package org.simplehttp.common.exception;

import lombok.Data;

/**
 * 为当前的错误状态保留一份组件快照
 */
@Data
public class SnapShotException extends Exception{
    // 请求 URL
    private String url;
    // 请求方法
    private String requestMethod;
}
