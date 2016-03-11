package com.hunantv.bigdata.troy.tools;

import com.hunantv.bigdata.troy.output.troyFileAppender.DailyRollingTroyFileAppender;
import com.hunantv.bigdata.troy.output.troyFileAppender.RollingTroyFileAppender;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.log4j.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

/**
 * Created by wuxinyong on 15-1-21.
 */
public class Utils {
    public static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        return  simpleDateFormat.format(new Date());
    }

    public static long getUnixTimeStamp(){
        return new Date().getTime();
    }

    /**
     * 返回服务器错误
     *
     * @param ctx
     * @param status
     */
    public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer("".getBytes()));
        response.headers().set(Constants.CONTENT_TYPE, "text/plain; Charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static String formatString(String value){
        return value == null ? "" : value;
    }

    public static Logger createLogger(String name, String fullPath, String split){
        Logger logger = Logger.getLogger(name);
        try {
            if (logger.getAppender("etl-log") == null) {
                Layout layout = new PatternLayout("%m%n");
                String suffix = null;
                int maxFileSize=0;
                if("week".equals(split)) {
                    suffix = "'.'yyyyMMww'.log'";
                } else if("day".equals(split)) {
                    suffix = "'.'yyyyMMdd'.log'";
                } else if("hour".equals(split)) {
                    suffix = "'.'yyyyMMddHH'.log'";
                } else if("minute".equals(split)) {
                    suffix = "'.'yyyyMMddHHmm'.log'";
                } else if("second".equals(split)) {
                    suffix = "'.'yyyyMMddHHmmss'.log'";
                } else {
                    if (split.matches("^\\d+$")) {
                        maxFileSize = Integer.parseInt(split) * 1024 * 1024;
                    } else {
                        suffix = "'.'yyyyMMdd'.log'";
                    }
                }

                Appender appender = null;

                if(null != suffix && !suffix.isEmpty()){
                    appender = new DailyRollingFileAppender(layout, fullPath,
                            suffix);
                }
                // 大小格式切分
                else if(maxFileSize>0){
                    appender = new RollingFileAppender(null, fullPath);
                    ((RollingFileAppender)appender).setMaximumFileSize(maxFileSize);
                }

//                Appender appender = new DailyRollingFileAppender(layout, fullPath,
//                        suffix);
                appender.setName("etl-log");
                logger.addAppender(appender);
                logger.setLevel(Level.INFO);
                logger.setAdditivity(false); // avoid logging in root logger file.
            }
        }catch(Exception e){
//            Log.error("new logger error.", e);
        }
        return logger;
    }
}
