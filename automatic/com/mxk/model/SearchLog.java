package com.mxk.model;

import java.util.Date;

public class SearchLog {
    private Integer id;

    private String keyword;

    private String searchFormIp;

    private Integer searchFromUser;

    private Date createTime;

    private Integer searchFromSite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getSearchFormIp() {
        return searchFormIp;
    }

    public void setSearchFormIp(String searchFormIp) {
        this.searchFormIp = searchFormIp == null ? null : searchFormIp.trim();
    }

    public Integer getSearchFromUser() {
        return searchFromUser;
    }

    public void setSearchFromUser(Integer searchFromUser) {
        this.searchFromUser = searchFromUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSearchFromSite() {
        return searchFromSite;
    }

    public void setSearchFromSite(Integer searchFromSite) {
        this.searchFromSite = searchFromSite;
    }
}