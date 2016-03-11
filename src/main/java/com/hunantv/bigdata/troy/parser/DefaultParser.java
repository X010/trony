package com.hunantv.bigdata.troy.parser;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LogConfig;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.JsonUtils;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 默认解析实现类
 * Created by wuxinyong on 15-1-22.
 */
public class DefaultParser implements IParser {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultParser.class);

    private AbstractConfigManager configManager;

    public DefaultParser(AbstractConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public Message parse(Message message) {
        if (message.getMethod() == HttpMethod.GET) {
            message = parseGet(message);
        } else if (message.getMethod() == HttpMethod.POST) {
            message = parseJsonPost(message);
        }
        return message;
    }

    private Message parseGet(Message message){
        String rawLog = message.getRawLog();
        Map<String, Object> keyValues = null;
        if (rawLog != null && rawLog.indexOf("&") >= 0) {
            String[] values = rawLog.split("&");
            keyValues = Maps.newHashMap();
            for (String value : values) {
                if (value.indexOf("=") >= 0) {
                    String[] keyValue = value.split("=");
                    if (keyValue != null && keyValue.length >= 1) {
                        if(keyValue.length >= 2) {
                            keyValues.put(keyValue[0], keyValue[1]);
                        } else {
                            keyValues.put(keyValue[0], "");
                        }

                    } else {
                        message.setStatus(MessageStatus.PARSE_ERROR);
                        message.setErrorInfo("Parse failed for GET parameter in invalid format. pair entry keyValue is null Or length < 1 , " + value);
                        break;
                    }
                } else{
                    message.setStatus(MessageStatus.PARSE_ERROR);
                    message.setErrorInfo("Parse failed for GET parameter in invalid format. cannot find '=' after split , " +  value);
                    break;
                }
            }

        } else {
            message.setStatus(MessageStatus.PARSE_ERROR);
            message.setErrorInfo("Parse failed for GET parameter in invalid format. cannot split");
        }

        if(message.getStatus() != MessageStatus.PARSE_ERROR && keyValues != null) {
            message.setKeyValues(keyValues);
            checkBidAndAct(message);
        } else {
            message.setStatus(MessageStatus.PARSE_ERROR);
        }

        return message;
    }

    private Message parseJsonPost(Message message){
        String rawLog = message.getRawLog();
        try {
            if(rawLog.startsWith("{")) { // 单条Json
                Map<String, Object> keyValues = (Map<String, Object>) JsonUtils.StringToJsonVideo(rawLog, Map.class);
                if(keyValues != null) {
                    message.setKeyValues(keyValues);
                    if(message.getIsBatch()){
                        checkBidForBatch(message);
                    } else {
                        checkBidAndAct(message);
                    }
                } else {
                    message.setStatus(MessageStatus.PARSE_ERROR);
                    message.setErrorInfo("Json parse failed.");
                }
            } else if (rawLog.startsWith("[")) { // Json数组，批量上报
                JSONArray jsons = (JSONArray)JsonUtils.StringToJsonArray(rawLog);
                if(jsons != null && jsons.size() > 0) {
                    Map<String, Object> firstJson = (Map<String, Object>)JsonUtils.StringToJsonVideo(jsons.getString(0), Map.class);
                    if(firstJson != null && firstJson.get(Constants.KEY_BID) != null) {
                        checkBid(firstJson.get(Constants.KEY_BID), message);
                        message.setIsBatch(Boolean.TRUE);
                        Object debugObj = firstJson.get(Constants.KEY_IS_DEBUG);
                        if(debugObj != null && String.valueOf(debugObj).equals(Constants.VALUE_IS_DEBUG)){
                            message.setStatus(MessageStatus.IS_DEBUG);
                        }
                    } else {
                        message.setStatus(MessageStatus.PARSE_ERROR);
                        message.setErrorInfo("Json in invalid format or no bid reported.");
                    }
                } else {
                    message.setStatus(MessageStatus.PARSE_ERROR);
                    message.setErrorInfo("Json in invalid format.");
                }
            } else {
                message.setStatus(MessageStatus.PARSE_ERROR);
                message.setErrorInfo("Json in invalid format.");
            }
        } catch (Exception e) {
            message.setStatus(MessageStatus.PARSE_ERROR);
            message.setErrorInfo("Json parse failed. Exception: " + e);
        }

        return message;
    }

    private void checkBidAndAct(Message message) {
        Object bid = message.getKeyValues().get(Constants.KEY_BID);

        if(checkBid(bid, message)) {
            message.setBid(String.valueOf(bid));

            Object actObj = message.getKeyValues().get(Constants.KEY_ACT);
//            if(actObj == null || Strings.isNullOrEmpty(String.valueOf(actObj))){
//                message.setStatus(MessageStatus.PARSE_ERROR);
//                message.setErrorInfo("No act reported.");
//                return;
//            }

            LogConfig config = configManager.getConfigMap().get(String.valueOf(bid).hashCode());
            Map<String, List<String>> flows = config.getFlows();
            boolean isFlowSet = false;
            for(String key : flows.keySet()){
                List<String> acts = flows.get(key);
                if(actObj != null && acts != null && acts.size() > 0) {
                    for(String act : acts){
                        if(String.valueOf(actObj).equalsIgnoreCase(act)){
                            message.setFlow(key);
                            isFlowSet = true;
                            break;
                        }
                    }
                    if(isFlowSet){
                        break;
                    }
                }
            }

            Object debugObj = message.getKeyValues().get(Constants.KEY_IS_DEBUG);
            if(debugObj != null && String.valueOf(debugObj).equals(Constants.VALUE_IS_DEBUG)){
                message.setStatus(MessageStatus.IS_DEBUG);
                return;
            }
            if(!isFlowSet) {
                message.setFlow(Constants.DEFAULT_FLOW);
            }
            message.setStatus(MessageStatus.PARSE_SUCC);
        }

    }

    private boolean checkBid(Object bid, Message message){

        if(bid == null || Strings.isNullOrEmpty(String.valueOf(bid))) {
            message.setStatus(MessageStatus.PARSE_ERROR);
            message.setBid(Constants.INVALID_BID);
            message.setErrorInfo("No bid reported.");
            return false;
        }

        LogConfig config = configManager.getConfigMap().get(String.valueOf(bid).hashCode());
        if(config == null){
            message.setBid(Constants.INVALID_BID);
            message.setStatus(MessageStatus.PARSE_ERROR);
            message.setErrorInfo("The bid is not configured. bid = " + String.valueOf(bid));
            return false;
        } else {
            message.setBid(String.valueOf(bid));
            message.setStatus(MessageStatus.PARSE_SUCC);
            return true;
        }
    }

    private void checkBidForBatch(Message message){
        Object common = message.getKeyValues().get(Constants.KEY_COMMON);
        Map<String, Object> commonParams = (Map<String, Object>)common;
        Object bid = commonParams.get(Constants.KEY_BID);
        if(checkBid(bid, message)) {
            message.setBid(String.valueOf(bid));
            Object debug = commonParams.get(Constants.KEY_IS_DEBUG);
            if(debug != null && Constants.VALUE_IS_DEBUG.equals(String.valueOf(debug))){
                message.setStatus(MessageStatus.IS_DEBUG);
                return;
            }
            message.setStatus(MessageStatus.PARSE_SUCC);
        }
    }

    @Override
    public void doStart() {

    }

    @Override
    public void destory() {

    }
}
