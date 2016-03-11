package com.hunantv.bigdata.troy.statistics;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LogConfig;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.entity.Statistics;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.JsonUtils;
import com.hunantv.bigdata.troy.tools.Utils;

import java.util.Map;

/**
 * 日志统计类
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/20
 * Time: PM7:16
 */
public class StatisticsUtils implements Lifecycle {

    public static Map<String, Statistics> configedStats;

    public static Map<String, Statistics> nonConfigedStats;

    public static Statistics totalStat;

    public StatisticsUtils(AbstractConfigManager configManager){
        Map<Integer, LogConfig> logConfigs = configManager.getConfigMap();
        configedStats = Maps.newHashMap();
        nonConfigedStats = Maps.newHashMap();

        Statistics stat;
        String time = Utils.getCurrentTime();
        long timestamp = Utils.getUnixTimeStamp();

        for(LogConfig config: logConfigs.values()){
            for(StatStatus status : StatStatus.values()){
                stat = new Statistics();
                stat.setBid(config.getBid());
                stat.setStartTime(time);
                stat.setStartTimeStamp(timestamp);
                stat.setLastTime(time);
                stat.setLastTimeStamp(timestamp);
                stat.setTotalCounter(0);
                configedStats.put(config.getBid() + "_" + status, stat);

                stat = new Statistics();
                stat.setBid(Constants.INVALID_BID);
                stat.setStartTime(time);
                stat.setStartTimeStamp(timestamp);
                stat.setLastTime(time);
                stat.setLastTimeStamp(timestamp);
                stat.setTotalCounter(0);
                nonConfigedStats.put(Constants.INVALID_BID + "_" + status, stat);
            }
        }

        totalStat = new Statistics();
        totalStat.setBid(Constants.TOTAL_BID);
        totalStat.setStartTime(time);
        totalStat.setStartTimeStamp(timestamp);
        totalStat.setLastTime(time);
        totalStat.setLastTimeStamp(timestamp);
        totalStat.setTotalCounter(0);

    }

    public static Message stat(Message message) {
        stat(message.getBid(), message.getStatus());
        return message;
    }

    public static void stat(String bid, MessageStatus status) {
        Statistics stat;
        if(!Strings.isNullOrEmpty(bid) && configedStats.get(bid + "_" + status) != null){
            stat = configedStats.get(bid + "_" + status);
        } else {
            stat = nonConfigedStats.get(Constants.INVALID_BID + "_" + status);
        }
        stat.increment();
    }

    public static void statTotal(){
        totalStat.increment();
    }

    public static String getStats(){
        Map<String, Statistics> stats = Maps.newHashMap();
        stats.put(Constants.TOTAL_BID, totalStat);
        stats.putAll(configedStats);
        stats.putAll(nonConfigedStats);
        return JsonUtils.simpleJson(stats);
    }

    private static enum StatStatus {
        BUILD_ERROR, PARSE_ERROR, VALID_SUCC, VALID_ERROR, IS_DEBUG,
    }

    @Override
    public void doStart() {

    }

    @Override
    public void destory() {

    }

}
