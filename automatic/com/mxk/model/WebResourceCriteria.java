package com.mxk.model;

import java.util.ArrayList;
import java.util.List;

public class WebResourceCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WebResourceCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andImageIsNull() {
            addCriterion("image is null");
            return (Criteria) this;
        }

        public Criteria andImageIsNotNull() {
            addCriterion("image is not null");
            return (Criteria) this;
        }

        public Criteria andImageEqualTo(String value) {
            addCriterion("image =", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotEqualTo(String value) {
            addCriterion("image <>", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThan(String value) {
            addCriterion("image >", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThanOrEqualTo(String value) {
            addCriterion("image >=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThan(String value) {
            addCriterion("image <", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThanOrEqualTo(String value) {
            addCriterion("image <=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLike(String value) {
            addCriterion("image like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotLike(String value) {
            addCriterion("image not like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageIn(List<String> values) {
            addCriterion("image in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotIn(List<String> values) {
            addCriterion("image not in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageBetween(String value1, String value2) {
            addCriterion("image between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotBetween(String value1, String value2) {
            addCriterion("image not between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andOwnernameIsNull() {
            addCriterion("ownername is null");
            return (Criteria) this;
        }

        public Criteria andOwnernameIsNotNull() {
            addCriterion("ownername is not null");
            return (Criteria) this;
        }

        public Criteria andOwnernameEqualTo(String value) {
            addCriterion("ownername =", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameNotEqualTo(String value) {
            addCriterion("ownername <>", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameGreaterThan(String value) {
            addCriterion("ownername >", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameGreaterThanOrEqualTo(String value) {
            addCriterion("ownername >=", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameLessThan(String value) {
            addCriterion("ownername <", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameLessThanOrEqualTo(String value) {
            addCriterion("ownername <=", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameLike(String value) {
            addCriterion("ownername like", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameNotLike(String value) {
            addCriterion("ownername not like", value, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameIn(List<String> values) {
            addCriterion("ownername in", values, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameNotIn(List<String> values) {
            addCriterion("ownername not in", values, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameBetween(String value1, String value2) {
            addCriterion("ownername between", value1, value2, "ownername");
            return (Criteria) this;
        }

        public Criteria andOwnernameNotBetween(String value1, String value2) {
            addCriterion("ownername not between", value1, value2, "ownername");
            return (Criteria) this;
        }

        public Criteria andSitenameIsNull() {
            addCriterion("sitename is null");
            return (Criteria) this;
        }

        public Criteria andSitenameIsNotNull() {
            addCriterion("sitename is not null");
            return (Criteria) this;
        }

        public Criteria andSitenameEqualTo(String value) {
            addCriterion("sitename =", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameNotEqualTo(String value) {
            addCriterion("sitename <>", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameGreaterThan(String value) {
            addCriterion("sitename >", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameGreaterThanOrEqualTo(String value) {
            addCriterion("sitename >=", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameLessThan(String value) {
            addCriterion("sitename <", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameLessThanOrEqualTo(String value) {
            addCriterion("sitename <=", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameLike(String value) {
            addCriterion("sitename like", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameNotLike(String value) {
            addCriterion("sitename not like", value, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameIn(List<String> values) {
            addCriterion("sitename in", values, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameNotIn(List<String> values) {
            addCriterion("sitename not in", values, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameBetween(String value1, String value2) {
            addCriterion("sitename between", value1, value2, "sitename");
            return (Criteria) this;
        }

        public Criteria andSitenameNotBetween(String value1, String value2) {
            addCriterion("sitename not between", value1, value2, "sitename");
            return (Criteria) this;
        }

        public Criteria andSiteurlIsNull() {
            addCriterion("siteurl is null");
            return (Criteria) this;
        }

        public Criteria andSiteurlIsNotNull() {
            addCriterion("siteurl is not null");
            return (Criteria) this;
        }

        public Criteria andSiteurlEqualTo(String value) {
            addCriterion("siteurl =", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlNotEqualTo(String value) {
            addCriterion("siteurl <>", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlGreaterThan(String value) {
            addCriterion("siteurl >", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlGreaterThanOrEqualTo(String value) {
            addCriterion("siteurl >=", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlLessThan(String value) {
            addCriterion("siteurl <", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlLessThanOrEqualTo(String value) {
            addCriterion("siteurl <=", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlLike(String value) {
            addCriterion("siteurl like", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlNotLike(String value) {
            addCriterion("siteurl not like", value, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlIn(List<String> values) {
            addCriterion("siteurl in", values, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlNotIn(List<String> values) {
            addCriterion("siteurl not in", values, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlBetween(String value1, String value2) {
            addCriterion("siteurl between", value1, value2, "siteurl");
            return (Criteria) this;
        }

        public Criteria andSiteurlNotBetween(String value1, String value2) {
            addCriterion("siteurl not between", value1, value2, "siteurl");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andInfoIsNull() {
            addCriterion("info is null");
            return (Criteria) this;
        }

        public Criteria andInfoIsNotNull() {
            addCriterion("info is not null");
            return (Criteria) this;
        }

        public Criteria andInfoEqualTo(String value) {
            addCriterion("info =", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotEqualTo(String value) {
            addCriterion("info <>", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoGreaterThan(String value) {
            addCriterion("info >", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoGreaterThanOrEqualTo(String value) {
            addCriterion("info >=", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLessThan(String value) {
            addCriterion("info <", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLessThanOrEqualTo(String value) {
            addCriterion("info <=", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoLike(String value) {
            addCriterion("info like", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotLike(String value) {
            addCriterion("info not like", value, "info");
            return (Criteria) this;
        }

        public Criteria andInfoIn(List<String> values) {
            addCriterion("info in", values, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotIn(List<String> values) {
            addCriterion("info not in", values, "info");
            return (Criteria) this;
        }

        public Criteria andInfoBetween(String value1, String value2) {
            addCriterion("info between", value1, value2, "info");
            return (Criteria) this;
        }

        public Criteria andInfoNotBetween(String value1, String value2) {
            addCriterion("info not between", value1, value2, "info");
            return (Criteria) this;
        }

        public Criteria andMultiinfoIsNull() {
            addCriterion("multiinfo is null");
            return (Criteria) this;
        }

        public Criteria andMultiinfoIsNotNull() {
            addCriterion("multiinfo is not null");
            return (Criteria) this;
        }

        public Criteria andMultiinfoEqualTo(String value) {
            addCriterion("multiinfo =", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoNotEqualTo(String value) {
            addCriterion("multiinfo <>", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoGreaterThan(String value) {
            addCriterion("multiinfo >", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoGreaterThanOrEqualTo(String value) {
            addCriterion("multiinfo >=", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoLessThan(String value) {
            addCriterion("multiinfo <", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoLessThanOrEqualTo(String value) {
            addCriterion("multiinfo <=", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoLike(String value) {
            addCriterion("multiinfo like", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoNotLike(String value) {
            addCriterion("multiinfo not like", value, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoIn(List<String> values) {
            addCriterion("multiinfo in", values, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoNotIn(List<String> values) {
            addCriterion("multiinfo not in", values, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoBetween(String value1, String value2) {
            addCriterion("multiinfo between", value1, value2, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andMultiinfoNotBetween(String value1, String value2) {
            addCriterion("multiinfo not between", value1, value2, "multiinfo");
            return (Criteria) this;
        }

        public Criteria andHitsIsNull() {
            addCriterion("hits is null");
            return (Criteria) this;
        }

        public Criteria andHitsIsNotNull() {
            addCriterion("hits is not null");
            return (Criteria) this;
        }

        public Criteria andHitsEqualTo(Integer value) {
            addCriterion("hits =", value, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsNotEqualTo(Integer value) {
            addCriterion("hits <>", value, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsGreaterThan(Integer value) {
            addCriterion("hits >", value, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsGreaterThanOrEqualTo(Integer value) {
            addCriterion("hits >=", value, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsLessThan(Integer value) {
            addCriterion("hits <", value, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsLessThanOrEqualTo(Integer value) {
            addCriterion("hits <=", value, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsIn(List<Integer> values) {
            addCriterion("hits in", values, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsNotIn(List<Integer> values) {
            addCriterion("hits not in", values, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsBetween(Integer value1, Integer value2) {
            addCriterion("hits between", value1, value2, "hits");
            return (Criteria) this;
        }

        public Criteria andHitsNotBetween(Integer value1, Integer value2) {
            addCriterion("hits not between", value1, value2, "hits");
            return (Criteria) this;
        }

        public Criteria andPostsIsNull() {
            addCriterion("posts is null");
            return (Criteria) this;
        }

        public Criteria andPostsIsNotNull() {
            addCriterion("posts is not null");
            return (Criteria) this;
        }

        public Criteria andPostsEqualTo(Integer value) {
            addCriterion("posts =", value, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsNotEqualTo(Integer value) {
            addCriterion("posts <>", value, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsGreaterThan(Integer value) {
            addCriterion("posts >", value, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsGreaterThanOrEqualTo(Integer value) {
            addCriterion("posts >=", value, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsLessThan(Integer value) {
            addCriterion("posts <", value, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsLessThanOrEqualTo(Integer value) {
            addCriterion("posts <=", value, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsIn(List<Integer> values) {
            addCriterion("posts in", values, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsNotIn(List<Integer> values) {
            addCriterion("posts not in", values, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsBetween(Integer value1, Integer value2) {
            addCriterion("posts between", value1, value2, "posts");
            return (Criteria) this;
        }

        public Criteria andPostsNotBetween(Integer value1, Integer value2) {
            addCriterion("posts not between", value1, value2, "posts");
            return (Criteria) this;
        }

        public Criteria andInsignificanceIsNull() {
            addCriterion("insignificance is null");
            return (Criteria) this;
        }

        public Criteria andInsignificanceIsNotNull() {
            addCriterion("insignificance is not null");
            return (Criteria) this;
        }

        public Criteria andInsignificanceEqualTo(Integer value) {
            addCriterion("insignificance =", value, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceNotEqualTo(Integer value) {
            addCriterion("insignificance <>", value, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceGreaterThan(Integer value) {
            addCriterion("insignificance >", value, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceGreaterThanOrEqualTo(Integer value) {
            addCriterion("insignificance >=", value, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceLessThan(Integer value) {
            addCriterion("insignificance <", value, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceLessThanOrEqualTo(Integer value) {
            addCriterion("insignificance <=", value, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceIn(List<Integer> values) {
            addCriterion("insignificance in", values, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceNotIn(List<Integer> values) {
            addCriterion("insignificance not in", values, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceBetween(Integer value1, Integer value2) {
            addCriterion("insignificance between", value1, value2, "insignificance");
            return (Criteria) this;
        }

        public Criteria andInsignificanceNotBetween(Integer value1, Integer value2) {
            addCriterion("insignificance not between", value1, value2, "insignificance");
            return (Criteria) this;
        }

        public Criteria andSignificanceIsNull() {
            addCriterion("significance is null");
            return (Criteria) this;
        }

        public Criteria andSignificanceIsNotNull() {
            addCriterion("significance is not null");
            return (Criteria) this;
        }

        public Criteria andSignificanceEqualTo(Integer value) {
            addCriterion("significance =", value, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceNotEqualTo(Integer value) {
            addCriterion("significance <>", value, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceGreaterThan(Integer value) {
            addCriterion("significance >", value, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceGreaterThanOrEqualTo(Integer value) {
            addCriterion("significance >=", value, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceLessThan(Integer value) {
            addCriterion("significance <", value, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceLessThanOrEqualTo(Integer value) {
            addCriterion("significance <=", value, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceIn(List<Integer> values) {
            addCriterion("significance in", values, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceNotIn(List<Integer> values) {
            addCriterion("significance not in", values, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceBetween(Integer value1, Integer value2) {
            addCriterion("significance between", value1, value2, "significance");
            return (Criteria) this;
        }

        public Criteria andSignificanceNotBetween(Integer value1, Integer value2) {
            addCriterion("significance not between", value1, value2, "significance");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}