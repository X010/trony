package com.hunantv.bigdata.troy;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LogConfig;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2015, hunantv.com All Rights Reserved.
 * User: caolin  
 * MailTo:lin@e.hunantv.com
 * Date: 15/1/21
 * Time: 10:12
 * 加载配置后立即一次性创建所有目录，避免请求写文件时候判断创建
 */
public class DirCreator {
	/*
	 * 创建配置文件中的目录
	 */
	public static void createDir(AbstractConfigManager configManager) {
		if (configManager == null) {
			return;
		}
		String sp = File.separator;
		File dir = null;
		//错误目录
		dir = (new File(configManager.getCommErrorPath())).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//统计目录
		dir = (new File(configManager.getStatPath())).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//日志目录
		Map<Integer, LogConfig> configMap = configManager.getConfigMap();
		if (null == configMap) {
			return;
		}
		for (LogConfig logConfig : configMap.values()) {
			String bId = logConfig.getBid();
			String bName = bId.replace(".", "_");
			String logPath = logConfig.getLogPath();
			Map<String, List<String>> flows = logConfig.getFlows();
			//分流目录
			String logDirPath = null;
			for (String flow : flows.keySet()) {
				//默认目录
				logDirPath = logPath + (bName + sp) + (flow+sp);
				dir = new File(logDirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				//is_debug目录
				logDirPath = logPath + (bName + sp) + (flow +"_debug");
				dir = new File(logDirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				//error目录
				logDirPath = logPath + (bName + sp) + (flow+"_error");
				dir = new File(logDirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
		}
	}
}