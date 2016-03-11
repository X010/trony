package com.hunantv.bigdata.troy.configure;

import com.google.common.collect.Maps;
import com.hunantv.bigdata.troy.tools.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志配置类
 * Created by wuxinyong on 15-1-23.
 */
public class LogConfig {
    /**
     * 业务ID
     */
    private String bid;

    /**
     * HTTP请求方法
     */
    private String method;

    /**
     * 日志存储路径
     */
    private String logPath;

    /**
     * 日志文件切分策略
     */
    private String split;

    private List<ValidatorConfig> validations;

    /**
     * 分流配置map
     */
    private Map<String, List<String>> flows;

    /**
     * 配置的业务日志字段
     */
    private String[] keys;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public List<ValidatorConfig> getValidations() {
        return validations;
    }

    public void setValidations(List<ValidatorConfig> validations) {
        this.validations = validations;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public Map<String, List<String>> getFlows() {
        if(flows == null) {
            flows = Maps.newHashMap();
        }
        flows.put(Constants.DEFAULT_FLOW, new ArrayList<String>(0));
        return flows;
    }

    public void setFlows(Map<String, List<String>> flows) {
        this.flows = flows;
    }

}
