package com.hunantv.bigdata.troy.configure;

import com.google.common.base.Strings;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.JsonUtils;
import com.hunantv.bigdata.troy.tools.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从本地加载业务配置文件
 *
 * Created by wuxinyong on 15-4-13.
 */
public class LocalConfigManager extends AbstractConfigManager{

    private static final Logger LOG = LoggerFactory.getLogger(LocalConfigManager.class);


    private final String logConfigFile;

    public LocalConfigManager(String serverConfigFile, String logConfigFile){
        this.serverConfigFile = serverConfigFile;
        this.logConfigFile = logConfigFile;
    }

    @Override
    public void loadBussinessConfig() throws Exception {
        if (!Strings.isNullOrEmpty(logConfigFile)) {
            File file = new File(logConfigFile);
            if (file.exists()) {

                Properties properties = new Properties();
                properties.load(new FileInputStream(file));

                Set<Object> keys = properties.keySet();
                if (keys != null) {
                    for (Object key : keys) {
                        if (key.toString().indexOf("mapping") >= 0) {
                            String mapping = properties.getProperty(key
                                    .toString());
                            LogConfig logConfig = null;
                            try {
                                logConfig = (LogConfig) JsonUtils
                                        .StringToJsonVideo(mapping,
                                                LogConfig.class);
                            } catch(Exception e) {
                                LOG.error("parse failed for config: " + mapping, e);
                            }
                            if (logConfig != null) {
                                String bid = logConfig.getBid();
                                if(!Strings.isNullOrEmpty(bid)){
                                    configMap.put(bid.hashCode(), logConfig);
                                    String formatBid = logConfig.getBid().replace(".","_");
                                    List<String> flows = new ArrayList<String>();
                                    flows.add(Constants.DEFAULT_FLOW);
                                    if(logConfig.getFlows() != null && logConfig.getFlows().size() > 0){
                                        flows.addAll(logConfig.getFlows().keySet());
                                    }
                                    for(String flow : flows){
                                        //  /data/logs/mobile/2_3_0/default/2_3_0_default_access.log
                                        String logFullPath = logConfig.getLogPath() +  formatBid + File.separator + flow +
                                                File.separator + formatBid + "_" + flow  + "_access.log";
                                        loggerMap.put(logFullPath, Utils.createLogger(logFullPath,logFullPath,logConfig.getSplit()));

                                        String errorLogFullPath = logConfig.getLogPath()+ formatBid + File.separator + flow + Constants.STATUS_ERROR +
                                                File.separator + formatBid + "_" + flow + Constants.STATUS_ERROR  + "_access.log";
                                        loggerMap.put(errorLogFullPath, Utils.createLogger(errorLogFullPath,errorLogFullPath,Constants.DEFAULT_SPLIT));


                                        String debugLogFullPath = logConfig.getLogPath()+ formatBid + File.separator + flow + Constants.STATUS_DEBUG +
                                                File.separator + formatBid + "_" + flow  + Constants.STATUS_DEBUG  + "_access.log";
                                        loggerMap.put(debugLogFullPath, Utils.createLogger(debugLogFullPath,debugLogFullPath,Constants.DEFAULT_SPLIT));


                                    }

                                } else {
                                    LOG.error("bid is null or empty. config: " + mapping);
                                }

                            } else {
                                LOG.error("parsed result is null. config: " + mapping);
                            }
                        }
                    }
                } else {
                    throw new FileNotFoundException("log config file not found: " + logConfigFile);
                }
            }
        } else {
            throw new Exception("no log config file specified.");
        }
    }

}
