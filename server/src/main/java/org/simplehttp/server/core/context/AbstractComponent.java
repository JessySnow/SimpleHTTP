package org.simplehttp.server.core.context;

import lombok.Data;

/**
 * 服务器上下文组件
 */
@Data
abstract public class AbstractComponent {
    protected BaseServerContext context;

    public AbstractComponent(BaseServerContext context) {
        this.context = context;
    }
}
