package com.mxk.model;

public class BaseLink {
    private Integer id;

    private String matchurl;

    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatchurl() {
        return matchurl;
    }

    public void setMatchurl(String matchurl) {
        this.matchurl = matchurl == null ? null : matchurl.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}