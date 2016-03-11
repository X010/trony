package com.hunantv.bigdata.troy.entity;

import com.hunantv.bigdata.troy.tools.Utils;

/**
 * Created by wuxinyong on 15-1-26.
 */
public class Statistics {

    private String bid;
    private MessageStatus status;
    private String startTime;
    private long startTimeStamp;
    private String lastTime;
    private long lastTimeStamp;
    private volatile long totalCounter;

    public void increment(){
        totalCounter++;
        setLastTime(Utils.getCurrentTime());
        setLastTimeStamp(Utils.getUnixTimeStamp());
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getTotalCounter() {
        return totalCounter;
    }

    public void setTotalCounter(long totalCounter) {
        this.totalCounter = totalCounter;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long timeStamp) {
        this.startTimeStamp = timeStamp;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }
}
