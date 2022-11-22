package org.simplehttp.server.enums.pojo.biz;

import java.util.Date;

/**
 * 业务对象 -- 用户
 */
public class User {
    // 用户 id，唯一键
    private long id;

    // 用户的 Session 过期时间，非存储字段，在用户添加到 Session 中时被设置
    private Date expireDate;

    // 用户名
    private String userName;

    // 用户密码
    private String passWord;

    // 用户积分
    private int credit;


    public void setId(long id) {
        this.id = id;
    }


    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public long getId() {
        return id;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
