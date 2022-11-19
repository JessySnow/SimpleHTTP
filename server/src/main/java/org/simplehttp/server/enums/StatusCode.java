package org.simplehttp.server.enums;

/**
 * Http 请求响应码，根据当前处理的结果语义选择一个合适的
 * eg: 处理成功，选择 OK
 *     积分不够，选择 METHOD_NOT_ALLOWED
 *     请求缺少参数，或者参数的格式不正确，选择 BAD_REQUEST
 */
public enum StatusCode {
    OK("OK",200),
    NOT_FOUND("Not Found", 404),
    BAD_REQUEST("Bad Request", 400),
    METHOD_NOT_ALLOWED("Method Not Allowed", 405);
    private String status;
    private int code;

    StatusCode(String status, int code){
        this.status = status;
        this.code = code;
    }
}
