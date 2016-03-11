package com.hunantv.bigdata.troy.entity;

import com.hunantv.bigdata.troy.tools.Constants;
import io.netty.handler.codec.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/20
 * Time: PM6:51
 * 请求消息体
 */
public class Message implements Serializable {

    /**
     * 请求到达服务端的时间－yyyyMMddHHmmss
     */
    private String time;

    /**
     * 服务端解析的客户端IP
     */
    private String ip;

    /**
     * 业务ID
     */
    private String bid;

    /**
     * Content-Type
     */
    private String contentType;

    /**
     * 请求HOST
     */
    private String host;

    /**
     * 请求User-Agent
     */
    private String ua;

    /**
     * 接受协议
     */
    private String accept;

    /**
     * 语言
     */
    private String acceptLanguage;

    /**
     * 编码
     */
    private String acceptEncoding;

    /**
     * 请求Cookie
     */
    private String cookie;

    /**
     * 请求Header的referer
     */
    private String referer;

    /**
     * 链接
     */
    private String connection;

    /**
     * 原始日志
     */
    private String rawLog;

    /**
     * 解析后的key，value对
     */
    private Map<String, Object> keyValues;

    /**
     * 解析失败的错误信息
     */
    private String errorInfo;

    /**
     * 格式化后的日志
     */
    private String formatLog;

    /**
     * 方法
     */
    private HttpMethod method;

    /**
     *  消息状态
     *  VALID_SUCC, 校验成功
     *  VALID_ERROR, 校验失败
     *  PARSE_SUCC, 解析成功
     *  PARSE_ERROR, 解析失败
     *  BUILD_SUCC, 构建成功
     *  BUILD_ERROR, 构建失败
     *  IS_DEBUG, DEBUG日志
     */
    private MessageStatus status;

    /**
     * 日志输出流，用于支持参数分流
     */
    private String flow;

    /**
     * 是否批量上报
     * true 批量上报
     * false 非批量上报
     */
    private Boolean isBatch = false;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getRawLog() {
        return rawLog;
    }

    public void setRawLog(String rawLog) {
        this.rawLog = rawLog;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Object> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(Map<String, Object> keyValues) {
        this.keyValues = keyValues;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFormatLog() {
        return formatLog;
    }

    public void setFormatLog(String formatLog) {
        this.formatLog = formatLog;
    }

    public String getFlow() {
        if(flow == null){
            flow = Constants.DEFAULT_FLOW;
        }
        return flow;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    @Override
    public String toString() {
        return "";
    }

}
