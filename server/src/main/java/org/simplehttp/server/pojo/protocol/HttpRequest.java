package org.simplehttp.server.pojo.protocol;

import lombok.Data;


@Data
public class HttpRequest {
    private HttpHeader header;
    private HttpBody body;
    private URLWrapper urlWrapper;
}
