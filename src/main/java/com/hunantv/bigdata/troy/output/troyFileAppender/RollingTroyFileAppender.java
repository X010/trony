package com.hunantv.bigdata.troy.output.troyFileAppender;

import java.io.*;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) 2015, hunantv.com All Rights Reserved.
 * User: caolin 
 * MailTo:lin@e.hunantv.com
 * Date: 15/1/21
 * Time: 10:12 /
 * 切分文件类 按日期格式化切分
 */
public class RollingTroyFileAppender extends TroyFileAppender {
	private final static Logger Log = Logger.getLogger(RollingTroyFileAppender.class);

//	private static final org.slf4j.Logger Log = LoggerFactory.getLogger(RollingTroyFileAppender.class);

    private long maxFileSize; //当前文件 还可写入的最大值
    private int maxBackupIndex;//当前的文件名字后缀最大值

    public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}

	public void setMaxBackupIndex(int maxBackupIndex) {
		this.maxBackupIndex = maxBackupIndex;
	}

	public RollingTroyFileAppender() {
        this.maxFileSize = 10485760L;
        this.maxBackupIndex = 20;
        
    }

    public RollingTroyFileAppender(String fileName, long maxFileSize) {
    	this.fileName = fileName;
        this.maxFileSize = maxFileSize;
        this.maxBackupIndex = 20;
        this.activateOptions();
    }

    public void activateOptions() {
        super.activateOptions();
        if (this.maxBackupIndex <1) {
        	Log.error("文件备份要大于0");
            return;
        }
        if (this.maxFileSize <=0) {
        	Log.error("文件大小要大于0");
            return;
        }
    }

	/*  
	 * 写文件
	 */
	public void subAppend() {
		super.subAppend();
		if (this.maxFileSize <= 0) {
			Log.error("文件大小要大于0");
			return;
		}
		File file = new File(this.fileName);// 要写的当前文件
		long fileSize = file.length() + this.content.length();// 当前文件大小如果写了后的大小
		// 滚动生成新文件再写
		if (fileSize >= maxFileSize) {
			this.rollOver();
			return;
		}
		// 直接当前文件写
		try {
			this.setFile(this.fileName);
			return;
		} catch (IOException e) {
			Log.error("setFile 失败" + e);
			return;
		}
	}

	/**
	 * 滚动生成新文件再写
	 */
	private void rollOver() {
		boolean renameSucceeded = true;
		
		// 1、容许的最大值，如果将要溢出，则删除最后一个文件（即后缀值最大的文件）
		File file = new File(this.fileName + '.' + this.maxBackupIndex);
		if (file.exists()) {
			renameSucceeded = file.delete();
		}
		// 2、把已经存在的文件重命名，每个后缀加1，即最大的是最先生成的
		for (int indexSuffix = maxBackupIndex - 1; indexSuffix >= 1	&& renameSucceeded; indexSuffix--) {
			file = new File(this.fileName + "." + indexSuffix);
			if (file.exists()) {
				File target = new File(this.fileName + '.' + (indexSuffix + 1));
				renameSucceeded = file.renameTo(target);
			}
		}
		// 3、当前要滚动的后缀为1,然后写新文件
		if (renameSucceeded) {
			File target = new File(this.fileName + "." + 1);
			// closeFile();
			file = new File(this.fileName);
			renameSucceeded = file.renameTo(target);
			if (renameSucceeded){
				try {
					this.setFile(fileName);
					return;
				} catch (IOException e) {
					if (e instanceof InterruptedIOException) {
						Thread.currentThread().interrupt();
					}
					Log.error("setFile(" + fileName + ", false) call failed.");
				}
			}
		}
	}
}