package com.hunantv.bigdata.troy.output.troyFileAppender;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

/**
 * Copyright (c) 2015, hunantv.com All Rights Reserved. 
 * User: caolin 
 * MailTo:lin@e.hunantv.com 
 * Date: 15/1/21 Time: 10:12 
 * 切分文件类
 */
public class TroyFileAppender {
	private final static Logger Log = Logger.getLogger(TroyFileAppender.class);
	protected String fileName;
	protected String content;
	protected boolean fileAppend;
	protected boolean bufferedIO;
	protected int bufferSize;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public TroyFileAppender() {
	}

	public void activateOptions() {
		if (null == this.fileName) {
			Log.error("文件名不能问空！");
			return;
		}
	}

	public void subAppend() {
		if (null == this.fileName) {
			Log.error("文件名不能问空！");
			return;
		}
		if (null == this.content || this.content.length() <= 0) {
			Log.error("要写的内容大小要大于0");
			return;
		}
	}
	/* 
     * 写文件 内容
     */
    public synchronized void setFile(String fileName) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true)));
        bufferedWriter.write(this.content);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

	/*
     * 写文件 内容
     */
	public synchronized void setFile(BufferedWriter bufferedWriter) throws IOException {
		if (bufferedWriter == null){
			throw new IOException(" input bufferWriter is null");
		}
		bufferedWriter.write(this.content);
		bufferedWriter.flush();
	}

}