package com.mxk.model;

import java.util.Date;

public class UserCollect {
    private Integer id;

    private Integer userId;

    private Integer colletTarget;

    private Integer colletTargetType;

    private Date createTime;

    private String tag;

    private String simpleDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getColletTarget() {
        return colletTarget;
    }

    public void setColletTarget(Integer colletTarget) {
        this.colletTarget = colletTarget;
    }

    public Integer getColletTargetType() {
        return colletTargetType;
    }

    public void setColletTargetType(Integer colletTargetType) {
        this.colletTargetType = colletTargetType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc == null ? null : simpleDesc.trim();
    }
}