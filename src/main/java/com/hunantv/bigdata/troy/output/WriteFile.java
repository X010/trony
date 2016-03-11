package com.hunantv.bigdata.troy.output;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.*;

import com.hunantv.bigdata.troy.output.troyFileAppender.DailyRollingTroyFileAppender;
import com.hunantv.bigdata.troy.output.troyFileAppender.RollingTroyFileAppender;
import com.hunantv.bigdata.troy.output.troyFileAppender.TroyFileAppender;

public class WriteFile {
	private final static Logger Log = Logger.getLogger(WriteFile.class);


	public static void writeFile(String fileName, String content,String split) {

		//文件检查
		File file=new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.error("创建文件失败！");
				e.printStackTrace();
				return;
			}
		}

		//写文件前，获取切分文件配置
		String datePattern=null;
		long maxFileSize=0;

		if("week".equals(split)) {
			datePattern = "'.'yyyyMMww'.log'";
		} else if("day".equals(split)) {
			datePattern = "'.'yyyyMMdd'.log'";
		} else if("hour".equals(split)) {
			datePattern = "'.'yyyyMMddHH'.log'";
		} else if("minute".equals(split)) {
			datePattern = "'.'yyyyMMddHHmm'.log'";
		} else if("second".equals(split)) {
			datePattern = "'.'yyyyMMddHHmmss'.log'";
		} else {
			if (split.matches("^\\d+$")) {
				maxFileSize = Long.parseLong(split) * 1024 * 1024;
			} else {
				datePattern = "'.'yyyyMMdd'.log'";
			}
		}

		//写文件
		TroyFileAppender troyFileAppender=null;
		try {
			// 日期格式切分
			if(null != datePattern && !datePattern.isEmpty()){
				troyFileAppender = new DailyRollingTroyFileAppender(fileName,datePattern);
			}
			// 大小格式切分
			else if(maxFileSize>0){
				troyFileAppender = new RollingTroyFileAppender(fileName,maxFileSize);
			}
			// 格式切分错误
			else{
				Log.error("切分文件处理失败，未传入切分格式！");
				return;
			}
			troyFileAppender.setContent(content);
			troyFileAppender.subAppend();
			return;
		} catch (IOException e1) {
			Log.error("切分文件处理失败！");
			e1.printStackTrace();
		}
		return;
	}
}