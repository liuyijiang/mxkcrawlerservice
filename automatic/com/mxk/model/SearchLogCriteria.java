package com.mxk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchLogCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SearchLogCriteria() {
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

        public Criteria andKeywordIsNull() {
            addCriterion("keyword is null");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNotNull() {
            addCriterion("keyword is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordEqualTo(String value) {
            addCriterion("keyword =", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotEqualTo(String value) {
            addCriterion("keyword <>", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThan(String value) {
            addCriterion("keyword >", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThanOrEqualTo(String value) {
            addCriterion("keyword >=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThan(String value) {
            addCriterion("keyword <", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThanOrEqualTo(String value) {
            addCriterion("keyword <=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLike(String value) {
            addCriterion("keyword like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotLike(String value) {
            addCriterion("keyword not like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordIn(List<String> values) {
            addCriterion("keyword in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotIn(List<String> values) {
            addCriterion("keyword not in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordBetween(String value1, String value2) {
            addCriterion("keyword between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotBetween(String value1, String value2) {
            addCriterion("keyword not between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpIsNull() {
            addCriterion("search_form_ip is null");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpIsNotNull() {
            addCriterion("search_form_ip is not null");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpEqualTo(String value) {
            addCriterion("search_form_ip =", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpNotEqualTo(String value) {
            addCriterion("search_form_ip <>", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpGreaterThan(String value) {
            addCriterion("search_form_ip >", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpGreaterThanOrEqualTo(String value) {
            addCriterion("search_form_ip >=", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpLessThan(String value) {
            addCriterion("search_form_ip <", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpLessThanOrEqualTo(String value) {
            addCriterion("search_form_ip <=", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpLike(String value) {
            addCriterion("search_form_ip like", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpNotLike(String value) {
            addCriterion("search_form_ip not like", value, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpIn(List<String> values) {
            addCriterion("search_form_ip in", values, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpNotIn(List<String> values) {
            addCriterion("search_form_ip not in", values, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpBetween(String value1, String value2) {
            addCriterion("search_form_ip between", value1, value2, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFormIpNotBetween(String value1, String value2) {
            addCriterion("search_form_ip not between", value1, value2, "searchFormIp");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserIsNull() {
            addCriterion("search_from_user is null");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserIsNotNull() {
            addCriterion("search_from_user is not null");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserEqualTo(Integer value) {
            addCriterion("search_from_user =", value, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserNotEqualTo(Integer value) {
            addCriterion("search_from_user <>", value, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserGreaterThan(Integer value) {
            addCriterion("search_from_user >", value, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("search_from_user >=", value, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserLessThan(Integer value) {
            addCriterion("search_from_user <", value, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserLessThanOrEqualTo(Integer value) {
            addCriterion("search_from_user <=", value, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserIn(List<Integer> values) {
            addCriterion("search_from_user in", values, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserNotIn(List<Integer> values) {
            addCriterion("search_from_user not in", values, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserBetween(Integer value1, Integer value2) {
            addCriterion("search_from_user between", value1, value2, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andSearchFromUserNotBetween(Integer value1, Integer value2) {
            addCriterion("search_from_user not between", value1, value2, "searchFromUser");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteIsNull() {
            addCriterion("search_from_site is null");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteIsNotNull() {
            addCriterion("search_from_site is not null");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteEqualTo(Integer value) {
            addCriterion("search_from_site =", value, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteNotEqualTo(Integer value) {
            addCriterion("search_from_site <>", value, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteGreaterThan(Integer value) {
            addCriterion("search_from_site >", value, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteGreaterThanOrEqualTo(Integer value) {
            addCriterion("search_from_site >=", value, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteLessThan(Integer value) {
            addCriterion("search_from_site <", value, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteLessThanOrEqualTo(Integer value) {
            addCriterion("search_from_site <=", value, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteIn(List<Integer> values) {
            addCriterion("search_from_site in", values, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteNotIn(List<Integer> values) {
            addCriterion("search_from_site not in", values, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteBetween(Integer value1, Integer value2) {
            addCriterion("search_from_site between", value1, value2, "searchFromSite");
            return (Criteria) this;
        }

        public Criteria andSearchFromSiteNotBetween(Integer value1, Integer value2) {
            addCriterion("search_from_site not between", value1, value2, "searchFromSite");
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