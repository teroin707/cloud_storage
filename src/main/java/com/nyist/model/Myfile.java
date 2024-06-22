package com.nyist.model;


import java.util.Date;

public class Myfile {
    private Integer id;
    private String oldfilename;
    private String newfilename;
    private String ext;
    private String path;
    private float size;
    private String type;
    private int isimg;
    private int downcounts;
    private Date uploadtime;
    private Date changetime;

    private int isrecycle;
    private Integer user_id;
    //以下属性独立于数据库
    private String icon;
    private int ids;
    private String sort;
    private String sortway;

    @Override
    public String toString() {
        return "Myfile{" +
                "id=" + id +
                ", oldfilename='" + oldfilename + '\'' +
                ", newfilename='" + newfilename + '\'' +
                ", ext='" + ext + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", isimg=" + isimg +
                ", downcounts=" + downcounts +
                ", uploadtime=" + uploadtime +
                ", changetime=" + changetime +
                ", isrecycle=" + isrecycle +
                ", user_id=" + user_id +
                ", icon='" + icon + '\'' +
                ", ids=" + ids +
                ", sort='" + sort + '\'' +
                ", sortway='" + sortway + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldfilename() {
        return oldfilename;
    }

    public void setOldfilename(String oldfilename) {
        this.oldfilename = oldfilename;
    }

    public String getNewfilename() {
        return newfilename;
    }

    public void setNewfilename(String newfilename) {
        this.newfilename = newfilename;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsimg() {
        return isimg;
    }

    public void setIsimg(int isimg) {
        this.isimg = isimg;
    }

    public int getDowncounts() {
        return downcounts;
    }

    public void setDowncounts(int downcounts) {
        this.downcounts = downcounts;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Date getChangetime() {
        return changetime;
    }

    public void setChangetime(Date changetime) {
        this.changetime = changetime;
    }

    public int getIsrecycle() {
        return isrecycle;
    }

    public void setIsrecycle(int isrecycle) {
        this.isrecycle = isrecycle;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortway() {
        return sortway;
    }

    public void setSortway(String sortway) {
        this.sortway = sortway;
    }
}
