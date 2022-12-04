package org.simplehttp.server.core.session.impl;

import org.simplehttp.server.core.session.UserSession;
import org.simplehttp.server.enums.pojo.biz.User;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server Context 可选组件，提供 用户 Session 支持
 * TODO Session 每一个实例使用不同的过期时间
 */
public class ContextUserSession implements UserSession{
    /**
     * 并发 Map，提供 Session 支持
     */
    ConcurrentHashMap<String, User> contextSession;

    // Session 过期时间，默认 3600 -- 30 分钟
    private int expireTime;

    public ContextUserSession(){
        this.contextSession = new ConcurrentHashMap<>();
        this.expireTime = 3600;
    }

    /**
     * 自定义过期时间，单位 秒
     * @param expireTime 过期时间
     */
    public ContextUserSession(int expireTime){
        this();
        this.expireTime = expireTime;
    }

    /**
     * 向 Session 中添加用户，或者刷新用户的过期时间
     * @param cookie  用户的 Cookie 串
     * @param user 用户对象引用
     * @exception IllegalArgumentException 不允许忽略的运行时异常，需要由服务器返回 5** 响应
     */
    @Override
    public void addUser(String cookie, User user) throws IllegalArgumentException{
        if(cookie == null || cookie.isEmpty()){
            throw new IllegalArgumentException("Cookie 串不允许为空");
        }
        if(user == null || user.getId() < 0){
            throw new IllegalArgumentException("用户不允许为空，或者用户的 id 不合法");
        }

        // 计算过期时间
        Date expireDate = new Date(System.currentTimeMillis() + (long) expireTime * 1000);
        user.setExpireDate(expireDate);
        contextSession.put(cookie, user);
    }

    /**
     * 从 Session 中删除用户
     * @param cookie 用户的 cookie 串
     * @param user 用户对象的引用
     */
    @Override
    public void delUser(String cookie, User user) throws RuntimeException{
        if(cookie == null || cookie.isEmpty()){
            throw new IllegalArgumentException("Cookie 串不允许为空");
        }
        if(user == null || user.getId() < 0){
            throw new IllegalArgumentException("用户不允许为空，或者用户的 id 不合法");
        }

        boolean removed = contextSession.remove(cookie, user);
        if(!removed){
            throw new RuntimeException("用户删除失败");
        }
    }

    /**
     * 从 Session 中获取用户
     * @param cookie 用户的 cookie 串
     */
    @Override
    public User getUser(String cookie){
        return contextSession.get(cookie);
    }

    /**
     * 启动一个守护线程，并尝试以低优先级运行
     */
    @Override
    public void gc() {
        Thread myGcThread = new Thread(new myGc(),"My Gc Thread");
        myGcThread.setDaemon(true);
        myGcThread.setPriority(Thread.MIN_PRIORITY);
        myGcThread.start();
    }

    /**
     * 负责清理过期的用户
     * TODO Java 定时任务调度
     */
    private class myGc implements Runnable{
        @Override
        public void run() {
            for (Map.Entry<String, User> next : contextSession.entrySet()) {
                if(next.getValue().getExpireDate().getTime() - System.currentTimeMillis() < 0){
                    delUser(next.getKey(), next.getValue());
                }
            }
            try {
                // 两分钟定时清理一次
                Thread.sleep(12000);
            } catch (InterruptedException ignored) {}
        }
    }
}
