package com.hunantv.bigdata.troy.output.troyFileAppender;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2015, hunantv.com All Rights Reserved.
 * User: caolin
 * MailTo:lin@e.hunantv.com
 * Date: 15/1/21
 * Time: 10:12
 * 切分文件类 按日期格式化切分
 */
public class DailyRollingTroyFileAppender extends TroyFileAppender {
    private final static Logger Log = Logger.getLogger(DailyRollingTroyFileAppender.class);
    private String datePattern;
    Date now = new Date();
    SimpleDateFormat sdf;
    private static final Object lock = new Object();
    private static String scheduledFilename;


    private static ConcurrentHashMap<String, FileOutputStream> fosMap = new ConcurrentHashMap<String, FileOutputStream>();

    private static final Integer WriteBufferSize = 1024 * 10;

    public void setDatePattern(String pattern) {
        this.datePattern = pattern;
    }

    public String getDatePattern() {
        return this.datePattern;
    }

    public DailyRollingTroyFileAppender() {
        this.datePattern = "'.'yyyyMMdd'.log'"; //默认切分格式
        this.now = new Date();
    }

    public DailyRollingTroyFileAppender(String fileName, String datePattern) throws IOException {
        this.fileName = fileName;
        this.datePattern = datePattern;
        this.now = new Date();
        this.activateOptions();
    }

    public void activateOptions() {
        super.activateOptions();
        if (null == this.datePattern) {
            Log.error("格式必须初始化");
            return;
        }
        this.now.setTime(System.currentTimeMillis());
        this.sdf = new SimpleDateFormat(this.datePattern);
    }

    public void subAppend() {
        super.subAppend();

        if (null == this.datePattern) {
            Log.error("格式必须初始化");
            return;
        }
        synchronized (lock) {

        File file = new File(this.fileName);// 要写的当前文件

            scheduledFilename = this.fileName + this.sdf.format(new Date(file.lastModified()));// 上次的文件如果加了时间后缀后
            String datedFilename = this.fileName + this.sdf.format(this.now);

            // 滚动生成新文件再写
            if (!scheduledFilename.equals(datedFilename)) {
                this.rollOver();
                scheduledFilename = datedFilename;
                return;
            }
        }
        // 直接当前文件写
        FileOutputStream fos = null;
        try {
            this.setFile(this.fileName);
        } catch (IOException e) {
            Log.error("setFile 失败", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * 滚动生成新文件再写
     */
    private void rollOver() {
        // closeFile();
        // 1、有同名的则删除
        File target = new File(this.scheduledFilename);
        if (target.exists()) {
            Log.error("file exist, will delete it... scheduledFilename: " + scheduledFilename + ", size: " +  target.length());
            target.delete();
        }
        // 3、当前要滚动的加后缀,然后写新文件
        boolean renameSucceeded = new File(this.fileName).renameTo(target);
        if (renameSucceeded) {
            try {
                this.setFile(this.fileName);
                return;
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                Log.error("setFile(" + fileName + ", false) call failed.", e);
            }
        } else {
            Log.error("rename failed... scheduledFilename: " + scheduledFilename);
        }
    }
}