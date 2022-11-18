package org.simplehttp.server.handler.annonation;

import org.simplehttp.server.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识是一个处理器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
    /**
     * 必填项，路由的唯一标识，这个路径指的是指完整的路径，包括了 contextPath
     * eg：contextPath = /api，如果你要访问 /api/test?userid=1 这个资源
     *     那么这里的 routePath 就是 "/api/test"
     */
    String routePath();

    // 请求方法，默认是 GET
    RequestMethod method() default RequestMethod.GET;
}
