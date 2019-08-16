package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProposalExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProposalExample() {
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

        public Criteria andHashIsNull() {
            addCriterion("hash is null");
            return (Criteria) this;
        }

        public Criteria andHashIsNotNull() {
            addCriterion("hash is not null");
            return (Criteria) this;
        }

        public Criteria andHashEqualTo(String value) {
            addCriterion("hash =", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotEqualTo(String value) {
            addCriterion("hash <>", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashGreaterThan(String value) {
            addCriterion("hash >", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashGreaterThanOrEqualTo(String value) {
            addCriterion("hash >=", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLessThan(String value) {
            addCriterion("hash <", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLessThanOrEqualTo(String value) {
            addCriterion("hash <=", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLike(String value) {
            addCriterion("hash like", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotLike(String value) {
            addCriterion("hash not like", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashIn(List<String> values) {
            addCriterion("hash in", values, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotIn(List<String> values) {
            addCriterion("hash not in", values, "hash");
            return (Criteria) this;
        }

        public Criteria andHashBetween(String value1, String value2) {
            addCriterion("hash between", value1, value2, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotBetween(String value1, String value2) {
            addCriterion("hash not between", value1, value2, "hash");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("`type` is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("`type` is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("`type` like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("`type` not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("`type` not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andVerifierIsNull() {
            addCriterion("verifier is null");
            return (Criteria) this;
        }

        public Criteria andVerifierIsNotNull() {
            addCriterion("verifier is not null");
            return (Criteria) this;
        }

        public Criteria andVerifierEqualTo(String value) {
            addCriterion("verifier =", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotEqualTo(String value) {
            addCriterion("verifier <>", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierGreaterThan(String value) {
            addCriterion("verifier >", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierGreaterThanOrEqualTo(String value) {
            addCriterion("verifier >=", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierLessThan(String value) {
            addCriterion("verifier <", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierLessThanOrEqualTo(String value) {
            addCriterion("verifier <=", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierLike(String value) {
            addCriterion("verifier like", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotLike(String value) {
            addCriterion("verifier not like", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierIn(List<String> values) {
            addCriterion("verifier in", values, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotIn(List<String> values) {
            addCriterion("verifier not in", values, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierBetween(String value1, String value2) {
            addCriterion("verifier between", value1, value2, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotBetween(String value1, String value2) {
            addCriterion("verifier not between", value1, value2, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNameIsNull() {
            addCriterion("verifier_name is null");
            return (Criteria) this;
        }

        public Criteria andVerifierNameIsNotNull() {
            addCriterion("verifier_name is not null");
            return (Criteria) this;
        }

        public Criteria andVerifierNameEqualTo(String value) {
            addCriterion("verifier_name =", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotEqualTo(String value) {
            addCriterion("verifier_name <>", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameGreaterThan(String value) {
            addCriterion("verifier_name >", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameGreaterThanOrEqualTo(String value) {
            addCriterion("verifier_name >=", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameLessThan(String value) {
            addCriterion("verifier_name <", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameLessThanOrEqualTo(String value) {
            addCriterion("verifier_name <=", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameLike(String value) {
            addCriterion("verifier_name like", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotLike(String value) {
            addCriterion("verifier_name not like", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameIn(List<String> values) {
            addCriterion("verifier_name in", values, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotIn(List<String> values) {
            addCriterion("verifier_name not in", values, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameBetween(String value1, String value2) {
            addCriterion("verifier_name between", value1, value2, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotBetween(String value1, String value2) {
            addCriterion("verifier_name not between", value1, value2, "verifierName");
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

        public Criteria andNewVersionIsNull() {
            addCriterion("new_version is null");
            return (Criteria) this;
        }

        public Criteria andNewVersionIsNotNull() {
            addCriterion("new_version is not null");
            return (Criteria) this;
        }

        public Criteria andNewVersionEqualTo(String value) {
            addCriterion("new_version =", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotEqualTo(String value) {
            addCriterion("new_version <>", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionGreaterThan(String value) {
            addCriterion("new_version >", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionGreaterThanOrEqualTo(String value) {
            addCriterion("new_version >=", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionLessThan(String value) {
            addCriterion("new_version <", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionLessThanOrEqualTo(String value) {
            addCriterion("new_version <=", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionLike(String value) {
            addCriterion("new_version like", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotLike(String value) {
            addCriterion("new_version not like", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionIn(List<String> values) {
            addCriterion("new_version in", values, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotIn(List<String> values) {
            addCriterion("new_version not in", values, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionBetween(String value1, String value2) {
            addCriterion("new_version between", value1, value2, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotBetween(String value1, String value2) {
            addCriterion("new_version not between", value1, value2, "newVersion");
            return (Criteria) this;
        }

        public Criteria andParamNameIsNull() {
            addCriterion("param_name is null");
            return (Criteria) this;
        }

        public Criteria andParamNameIsNotNull() {
            addCriterion("param_name is not null");
            return (Criteria) this;
        }

        public Criteria andParamNameEqualTo(String value) {
            addCriterion("param_name =", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotEqualTo(String value) {
            addCriterion("param_name <>", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameGreaterThan(String value) {
            addCriterion("param_name >", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameGreaterThanOrEqualTo(String value) {
            addCriterion("param_name >=", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameLessThan(String value) {
            addCriterion("param_name <", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameLessThanOrEqualTo(String value) {
            addCriterion("param_name <=", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameLike(String value) {
            addCriterion("param_name like", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotLike(String value) {
            addCriterion("param_name not like", value, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameIn(List<String> values) {
            addCriterion("param_name in", values, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotIn(List<String> values) {
            addCriterion("param_name not in", values, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameBetween(String value1, String value2) {
            addCriterion("param_name between", value1, value2, "paramName");
            return (Criteria) this;
        }

        public Criteria andParamNameNotBetween(String value1, String value2) {
            addCriterion("param_name not between", value1, value2, "paramName");
            return (Criteria) this;
        }

        public Criteria andCurrentValueIsNull() {
            addCriterion("current_value is null");
            return (Criteria) this;
        }

        public Criteria andCurrentValueIsNotNull() {
            addCriterion("current_value is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentValueEqualTo(String value) {
            addCriterion("current_value =", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueNotEqualTo(String value) {
            addCriterion("current_value <>", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueGreaterThan(String value) {
            addCriterion("current_value >", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueGreaterThanOrEqualTo(String value) {
            addCriterion("current_value >=", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueLessThan(String value) {
            addCriterion("current_value <", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueLessThanOrEqualTo(String value) {
            addCriterion("current_value <=", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueLike(String value) {
            addCriterion("current_value like", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueNotLike(String value) {
            addCriterion("current_value not like", value, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueIn(List<String> values) {
            addCriterion("current_value in", values, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueNotIn(List<String> values) {
            addCriterion("current_value not in", values, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueBetween(String value1, String value2) {
            addCriterion("current_value between", value1, value2, "currentValue");
            return (Criteria) this;
        }

        public Criteria andCurrentValueNotBetween(String value1, String value2) {
            addCriterion("current_value not between", value1, value2, "currentValue");
            return (Criteria) this;
        }

        public Criteria andNewValueIsNull() {
            addCriterion("new_value is null");
            return (Criteria) this;
        }

        public Criteria andNewValueIsNotNull() {
            addCriterion("new_value is not null");
            return (Criteria) this;
        }

        public Criteria andNewValueEqualTo(String value) {
            addCriterion("new_value =", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueNotEqualTo(String value) {
            addCriterion("new_value <>", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueGreaterThan(String value) {
            addCriterion("new_value >", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueGreaterThanOrEqualTo(String value) {
            addCriterion("new_value >=", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueLessThan(String value) {
            addCriterion("new_value <", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueLessThanOrEqualTo(String value) {
            addCriterion("new_value <=", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueLike(String value) {
            addCriterion("new_value like", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueNotLike(String value) {
            addCriterion("new_value not like", value, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueIn(List<String> values) {
            addCriterion("new_value in", values, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueNotIn(List<String> values) {
            addCriterion("new_value not in", values, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueBetween(String value1, String value2) {
            addCriterion("new_value between", value1, value2, "newValue");
            return (Criteria) this;
        }

        public Criteria andNewValueNotBetween(String value1, String value2) {
            addCriterion("new_value not between", value1, value2, "newValue");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockIsNull() {
            addCriterion("end_voting_block is null");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockIsNotNull() {
            addCriterion("end_voting_block is not null");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockEqualTo(String value) {
            addCriterion("end_voting_block =", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockNotEqualTo(String value) {
            addCriterion("end_voting_block <>", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockGreaterThan(String value) {
            addCriterion("end_voting_block >", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockGreaterThanOrEqualTo(String value) {
            addCriterion("end_voting_block >=", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockLessThan(String value) {
            addCriterion("end_voting_block <", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockLessThanOrEqualTo(String value) {
            addCriterion("end_voting_block <=", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockLike(String value) {
            addCriterion("end_voting_block like", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockNotLike(String value) {
            addCriterion("end_voting_block not like", value, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockIn(List<String> values) {
            addCriterion("end_voting_block in", values, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockNotIn(List<String> values) {
            addCriterion("end_voting_block not in", values, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockBetween(String value1, String value2) {
            addCriterion("end_voting_block between", value1, value2, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andEndVotingBlockNotBetween(String value1, String value2) {
            addCriterion("end_voting_block not between", value1, value2, "endVotingBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockIsNull() {
            addCriterion("active_block is null");
            return (Criteria) this;
        }

        public Criteria andActiveBlockIsNotNull() {
            addCriterion("active_block is not null");
            return (Criteria) this;
        }

        public Criteria andActiveBlockEqualTo(String value) {
            addCriterion("active_block =", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotEqualTo(String value) {
            addCriterion("active_block <>", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockGreaterThan(String value) {
            addCriterion("active_block >", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockGreaterThanOrEqualTo(String value) {
            addCriterion("active_block >=", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockLessThan(String value) {
            addCriterion("active_block <", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockLessThanOrEqualTo(String value) {
            addCriterion("active_block <=", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockLike(String value) {
            addCriterion("active_block like", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotLike(String value) {
            addCriterion("active_block not like", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockIn(List<String> values) {
            addCriterion("active_block in", values, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotIn(List<String> values) {
            addCriterion("active_block not in", values, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockBetween(String value1, String value2) {
            addCriterion("active_block between", value1, value2, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotBetween(String value1, String value2) {
            addCriterion("active_block not between", value1, value2, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNull() {
            addCriterion("`timestamp` is null");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNotNull() {
            addCriterion("`timestamp` is not null");
            return (Criteria) this;
        }

        public Criteria andTimestampEqualTo(Date value) {
            addCriterion("`timestamp` =", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotEqualTo(Date value) {
            addCriterion("`timestamp` <>", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThan(Date value) {
            addCriterion("`timestamp` >", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("`timestamp` >=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThan(Date value) {
            addCriterion("`timestamp` <", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThanOrEqualTo(Date value) {
            addCriterion("`timestamp` <=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampIn(List<Date> values) {
            addCriterion("`timestamp` in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotIn(List<Date> values) {
            addCriterion("`timestamp` not in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampBetween(Date value1, Date value2) {
            addCriterion("`timestamp` between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotBetween(Date value1, Date value2) {
            addCriterion("`timestamp` not between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andYeasIsNull() {
            addCriterion("yeas is null");
            return (Criteria) this;
        }

        public Criteria andYeasIsNotNull() {
            addCriterion("yeas is not null");
            return (Criteria) this;
        }

        public Criteria andYeasEqualTo(Long value) {
            addCriterion("yeas =", value, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasNotEqualTo(Long value) {
            addCriterion("yeas <>", value, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasGreaterThan(Long value) {
            addCriterion("yeas >", value, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasGreaterThanOrEqualTo(Long value) {
            addCriterion("yeas >=", value, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasLessThan(Long value) {
            addCriterion("yeas <", value, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasLessThanOrEqualTo(Long value) {
            addCriterion("yeas <=", value, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasIn(List<Long> values) {
            addCriterion("yeas in", values, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasNotIn(List<Long> values) {
            addCriterion("yeas not in", values, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasBetween(Long value1, Long value2) {
            addCriterion("yeas between", value1, value2, "yeas");
            return (Criteria) this;
        }

        public Criteria andYeasNotBetween(Long value1, Long value2) {
            addCriterion("yeas not between", value1, value2, "yeas");
            return (Criteria) this;
        }

        public Criteria andNaysIsNull() {
            addCriterion("nays is null");
            return (Criteria) this;
        }

        public Criteria andNaysIsNotNull() {
            addCriterion("nays is not null");
            return (Criteria) this;
        }

        public Criteria andNaysEqualTo(Long value) {
            addCriterion("nays =", value, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysNotEqualTo(Long value) {
            addCriterion("nays <>", value, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysGreaterThan(Long value) {
            addCriterion("nays >", value, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysGreaterThanOrEqualTo(Long value) {
            addCriterion("nays >=", value, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysLessThan(Long value) {
            addCriterion("nays <", value, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysLessThanOrEqualTo(Long value) {
            addCriterion("nays <=", value, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysIn(List<Long> values) {
            addCriterion("nays in", values, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysNotIn(List<Long> values) {
            addCriterion("nays not in", values, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysBetween(Long value1, Long value2) {
            addCriterion("nays between", value1, value2, "nays");
            return (Criteria) this;
        }

        public Criteria andNaysNotBetween(Long value1, Long value2) {
            addCriterion("nays not between", value1, value2, "nays");
            return (Criteria) this;
        }

        public Criteria andAbstentionsIsNull() {
            addCriterion("abstentions is null");
            return (Criteria) this;
        }

        public Criteria andAbstentionsIsNotNull() {
            addCriterion("abstentions is not null");
            return (Criteria) this;
        }

        public Criteria andAbstentionsEqualTo(Long value) {
            addCriterion("abstentions =", value, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsNotEqualTo(Long value) {
            addCriterion("abstentions <>", value, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsGreaterThan(Long value) {
            addCriterion("abstentions >", value, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsGreaterThanOrEqualTo(Long value) {
            addCriterion("abstentions >=", value, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsLessThan(Long value) {
            addCriterion("abstentions <", value, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsLessThanOrEqualTo(Long value) {
            addCriterion("abstentions <=", value, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsIn(List<Long> values) {
            addCriterion("abstentions in", values, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsNotIn(List<Long> values) {
            addCriterion("abstentions not in", values, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsBetween(Long value1, Long value2) {
            addCriterion("abstentions between", value1, value2, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAbstentionsNotBetween(Long value1, Long value2) {
            addCriterion("abstentions not between", value1, value2, "abstentions");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersIsNull() {
            addCriterion("accu_verifiers is null");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersIsNotNull() {
            addCriterion("accu_verifiers is not null");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersEqualTo(Long value) {
            addCriterion("accu_verifiers =", value, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersNotEqualTo(Long value) {
            addCriterion("accu_verifiers <>", value, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersGreaterThan(Long value) {
            addCriterion("accu_verifiers >", value, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersGreaterThanOrEqualTo(Long value) {
            addCriterion("accu_verifiers >=", value, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersLessThan(Long value) {
            addCriterion("accu_verifiers <", value, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersLessThanOrEqualTo(Long value) {
            addCriterion("accu_verifiers <=", value, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersIn(List<Long> values) {
            addCriterion("accu_verifiers in", values, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersNotIn(List<Long> values) {
            addCriterion("accu_verifiers not in", values, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersBetween(Long value1, Long value2) {
            addCriterion("accu_verifiers between", value1, value2, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andAccuVerifiersNotBetween(Long value1, Long value2) {
            addCriterion("accu_verifiers not between", value1, value2, "accuVerifiers");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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