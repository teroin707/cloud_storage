package com.nyist.model;

import java.util.Date;


public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String nickname;
    private float space;
    private float usespace;
    private int filecounts;
    private String avatar;
    private String newavatarname;
    private Date regdate;
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", nickname='" + nickname + '\'' +
                ", space=" + space +
                ", usespace=" + usespace +
                ", filecounts=" + filecounts +
                ", avatar='" + avatar + '\'' +
                ", newavatarname='" + newavatarname + '\'' +
                ", regdate=" + regdate +
                '}';
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public float getSpace() {
        return space;
    }

    public void setSpace(float space) {
        this.space = space;
    }

    public float getUsespace() {
        return usespace;
    }

    public void setUsespace(float usespace) {
        this.usespace = usespace;
    }

    public int getFilecounts() {
        return filecounts;
    }

    public void setFilecounts(int filecounts) {
        this.filecounts = filecounts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNewavatarname() {
        return newavatarname;
    }

    public void setNewavatarname(String newavatarname) {
        this.newavatarname = newavatarname;
    }


    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }
}
