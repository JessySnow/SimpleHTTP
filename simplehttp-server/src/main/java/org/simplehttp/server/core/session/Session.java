package org.simplehttp.server.core.session;

/**
 * Session 接口
 */
public interface Session<Key, Value> {
    Value add(Key k, Value v);
    boolean del(Key k, Value v);
    Value del(Key k);
    Value get(Key k);
    boolean contains(Key k);
}
