package com.hunantv.bigdata.troy;

import com.hunantv.bigdata.troy.output.troyFileAppender.DailyRollingTroyFileAppender;
import com.hunantv.bigdata.troy.output.troyFileAppender.RollingTroyFileAppender;

import java.io.File;
import java.io.IOException;

public class Maintest {

	public static void main(String[] args) throws IOException {
		String ss = null;
		System.out.println((ss == null ? "" : "null") + "xxx");
//		lenth() ;
//		timer();
//		rollingTroyFileAppenderWrite();
//		DailyrollingTroyFileAppenderWrite();
//		path();
		
//		writeFile();
//		TroyDailyRollingFileAppender();
	}
	public static void lenth() throws IOException {
		File file=new File("d:"+File.separator+"尚学堂马士兵_设计模式_责任链_04.avi");
		System.out.println((file.length()/1024)/1024); 
		String maxFileSize=" ";
		if(maxFileSize.matches("^\\d+$")){
			long w=Long.parseLong(maxFileSize);
			System.out.println(w); 
		}else{
			System.out.println("不是数字"); 
		}
	}
	public static void path() {
		System.out.println(Maintest.class.getClassLoader().getResource(""));//file:/D:/git/troy/bin/
		System.out.println(Maintest.class.getClassLoader().getResource("."));//file:/D:/git/troy/bin/
		System.out.println(Maintest.class.getClassLoader().getResource(".."));//null
		System.out.println(Maintest.class.getClassLoader().getResource("./"));//file:/D:/git/troy/bin/
		System.out.println(Maintest.class.getClassLoader().getResource("../"));//null
		System.out.println(Maintest.class.getClassLoader().getResource("./").getPath());// /D:/git/troy/bin/
		System.out.println(System.getProperty("user.dir"));// D:\git\troy
		System.out.println("-------------------"); 
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("")); //file:/D:/git/troy/bin/
		System.out.println(ClassLoader.getSystemResource("")); //file:/D:/git/troy/bin/
		System.out.println(Maintest.class.getResource("")); //file:/D:/git/troy/bin/com/hunantv/bigdata/troy/
		System.out.println(Maintest.class.getResource("/")); // file:/D:/git/troy/bin/
		System.out.println(new File("/").getAbsolutePath()); //D:\
	}
	public static void timer() {
		java.util.Timer timer = new java.util.Timer(true);
		java.util.TimerTask task = new java.util.TimerTask() {
			@Override
			public void run() {
				rollingTroyFileAppenderWrite();
				System.out.println("需要定时执行的任务.hhh..");
			}
		};
		long delay = 0;
		long period = 1000;
		timer.schedule(task, delay, period);
	}
//	public static void writeFile() {
//		IOutput output=new DefaultOutPut(new Parameter());
//		output.output(MessageStatus.VALID_SUCC,null,null,"info_测试_");
//	}
	public static void DailyrollingTroyFileAppenderWrite() throws IOException {
		DailyRollingTroyFileAppender r = new DailyRollingTroyFileAppender("D:"
				+ File.separator + "log.log", "yyyy-MM-dd-hh-mm-ss'.log'");
		for (int indexSuffix = 0; indexSuffix <20; indexSuffix++) {
			r.setContent("abcde"+indexSuffix+"\n");
			r.subAppend();
		}
	}

	public static void rollingTroyFileAppenderWrite() {
		RollingTroyFileAppender r = new RollingTroyFileAppender("D:"
				+ File.separator + "log.log", 20L);
		for (int indexSuffix = 0; indexSuffix <20; indexSuffix++) {
			r.setContent("abcde"+indexSuffix+"\n");
			r.subAppend();
		}
	}
}
