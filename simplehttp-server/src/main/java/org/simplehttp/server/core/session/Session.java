package org.simplehttp.server.core.session;

/**
 * Session 接口
 */
public interface Session<Key, Value> {
    void add(Key k, Value v);
    void del(Key k, Value v);
    Value get(Key k);
}
