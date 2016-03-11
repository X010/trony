package com.hunantv.bigdata.troy.format;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LogConfig;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.Utils;

/**
 * 默认格式化实现类，格式化日志便于输出
 * Created by wuxinyong on 15-1-22.
 */
public class DefaultFormatter implements IFormatter {

    private AbstractConfigManager configManager;

    public DefaultFormatter(AbstractConfigManager configManager){
        this.configManager = configManager;
    }

    @Override
    public Message format(Message message){
        StringBuilder formatLog = new StringBuilder();
        if(message.getStatus() == MessageStatus.BUILD_ERROR || message.getStatus() == MessageStatus.PARSE_ERROR){
            formatLog.append(Utils.getCurrentTime()).append(Constants.FORMAT_SPLIT).append(message.getIp()).append(Constants.FORMAT_SPLIT)
               .append(message.getErrorInfo()).append(Constants.FORMAT_SPLIT).append(message.getRawLog()).append(Constants.FORMAT_SPLIT);
        } else if (message.getStatus() == MessageStatus.IS_DEBUG){
            formatLog.append(Utils.getCurrentTime()).append(Constants.FORMAT_SPLIT).append(message.getIp()).append(Constants.FORMAT_SPLIT)
                    .append(message.getRawLog()).append(Constants.FORMAT_SPLIT);
        } else {
            formatLog.append(Utils.getCurrentTime()).append(Constants.FORMAT_SPLIT).append(message.getIp()).append(Constants.FORMAT_SPLIT);

            if(message.getIsBatch()) {
                formatLog.append(message.getRawLog()).append(Constants.FORMAT_SPLIT);
            } else {
                LogConfig config = configManager.getConfigMap().get(message.getBid().hashCode());
                String[] keys = config.getKeys();
                for (String key : keys) {
                    String valueStr = "";
                    Object value = message.getKeyValues().get(key);
                    if (value != null) {
                        valueStr = String.valueOf(value);
                    }
                    formatLog.append(valueStr).append(Constants.FORMAT_SPLIT);
                }
            }
        }
        // HTTP请求Method，以及header中的 host,ua,referer,cookie按照相应顺序在配置key对应value后面输出
        formatLog.append((message.getMethod() == null ? "" : message.getMethod())).append(Constants.FORMAT_SPLIT);
        formatLog.append((message.getHost() == null ? "" : message.getHost())).append(Constants.FORMAT_SPLIT);
        formatLog.append((message.getUa() == null ? "" : message.getUa())).append(Constants.FORMAT_SPLIT);
        formatLog.append((message.getReferer() == null ? "" : message.getReferer())).append(Constants.FORMAT_SPLIT);
        formatLog.append((message.getCookie() == null ? "" : message.getCookie())).append(Constants.FORMAT_SPLIT);

        if (message.getStatus() == MessageStatus.VALID_ERROR) {
            formatLog.append(Constants.VALIDATE_ERROR_FLAG);
        } else {
            formatLog.append(Constants.VALIDATE_SUCC_FLAG);
        }

        message.setFormatLog(formatLog.toString());
        return message;
    }

    @Override
    public void doStart() {

    }

    @Override
    public void destory() {

    }
}
