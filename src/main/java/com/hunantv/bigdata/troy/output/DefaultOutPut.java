package com.hunantv.bigdata.troy.output;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LogConfig;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.tools.Constants;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Map;

/**
 * Copyright (c) 2015, hunantv.com All Rights Reserved.
 * User: caolin  MailTo:lin@e.hunantv.com
 * Date: 15/1/21
 * Time: 10:12
 * 日志输出实现类
 */
public class DefaultOutPut implements IOutput{
	private final static Logger Log = Logger.getLogger(DefaultOutPut.class);
	private AbstractConfigManager configManager=null;//文件配置信息
	private String logSuffix="_access.log";
	private String sp=File.separator;

	public DefaultOutPut(AbstractConfigManager configManager){
		this.configManager=configManager;
	}
	/*
	 * 根据 bid 上发处理砖头 进行记录
	 */
	@Override
	public boolean output(MessageStatus messageStatus, String bId, String flow, String content) {
		// 记录文本的路径处理
		String commErrorPath =this.configManager.getCommErrorPath();
		String split=null;//切分格式 如：day

		String logFullPath=null;
		String logPath=null; // 目录第1层: logs/mobile/
		String bName=null;//目录第2层: 2_0_1/  如：logs/mobile/2_0_1/
//		String flow=null; //目录第3层: play_stop/ 如：logs/mobile/2_0_1/play_stop | play_stop_debug | play_stop_error

		//获取需要的路径、切分格式
		Map<Integer, LogConfig> configMap = this.configManager.getConfigMap();
		//bid 解析成功
		if (null != bId && !"".equals(bId) && !Constants.INVALID_BID.equals(bId)) {
			bName = bId.replace(".", "_");
			LogConfig logConfig = configMap.get(bId.hashCode());
			if (null != logConfig) {
				split = logConfig.getSplit();
				logPath = logConfig.getLogPath();
			}
			//处理状态
			if (null != messageStatus && (null != logPath && !"".equals(logPath)) && null != flow) {
				//正常日志
				if (MessageStatus.VALID_SUCC.equals(messageStatus) || MessageStatus.VALID_ERROR.equals(messageStatus)){
					logFullPath = logPath + (bName + sp) + (flow + sp) + (bName + "_" + flow + logSuffix);
				}
				//debug日志
				else if(MessageStatus.IS_DEBUG.equals(messageStatus)) {
					logFullPath = logPath + (bName + sp) + (flow + "_debug" + sp) + (bName + "_" + flow + "_debug" + logSuffix);
				}
				//错误日志
				else{
					logFullPath = logPath + (bName + sp) + (flow + "_error" + sp) + (bName + "_" + flow + "_error" + logSuffix);
				}
			} else {
				Log.error("传入参数：messageStatus||logPath||flow 错误！");
				return false;
			}
		}
		// bId 没有解释成功
		else {
			Log.error("bId 没有解释成功！");
			logFullPath = commErrorPath;
		}
		//写文件
		if(null==split){
			split="hour";
		}
		if(null==logFullPath){
			logFullPath=commErrorPath;
		}
		if(null==content){
			Log.error("content 没有解释成功！");
			return false;
		}

//		WriteFile.writeFile(logFullPath,content+"\n",split);
		writeLog(logFullPath, content);
		return true;
	}
	@Override
	public Message output(Message message) {
		return null;
	}
	@Override
	public void destory() {
	}
	@Override
	public void doStart() {
	}
	public boolean isNoNullAndNoEmpty(String string){
		return (null!=string&&!"".equals(string));
	}

	private void writeLog(String logFullPath, String content){
		Logger logger = configManager.getLoggerMap().get(logFullPath);
		if(logger != null){
			logger.info(content);
		}else{
			logger = configManager.getLoggerMap().get(this.configManager.getCommErrorPath());
			logger.info("cannot find logger. log full path: " + logFullPath + "\t" + content);
		}

	}
}