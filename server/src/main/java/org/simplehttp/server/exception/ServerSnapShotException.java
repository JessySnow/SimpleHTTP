package org.simplehttp.server.exception;

import lombok.Data;
import org.simplehttp.common.exception.SnapShotException;
import org.simplehttp.server.enums.StatusCode;

@Data
public class ServerSnapShotException extends SnapShotException {
    public ServerSnapShotException(){
        super();
    }

    public ServerSnapShotException(String url, String method, StatusCode code){
        this.url = url;
        this.requestMethod = method;
        this.code = code;
    }
    // 错误码
    protected StatusCode code;
}
