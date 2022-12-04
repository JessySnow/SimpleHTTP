package org.simplehttp.server.core.session;

import org.simplehttp.server.enums.pojo.biz.User;

public interface UserSession extends Session<String, User> {
    @Override
    default void add(String k, User v){
        this.addUser(k ,v);
    }

    @Override
    default void del(String k, User v){
        this.delUser(k, v);
    }

    @Override
    default User get(String k){
        return this.getUser(k);
    }

    @Override
    void gc();

    void addUser(String cookie, User user);
    void delUser(String cookie, User user);
    User getUser(String user);
}
