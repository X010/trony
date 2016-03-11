package com.hunantv.bigdata.troy.configure;

import com.google.common.base.Strings;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.Utils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理抽象类
 *
 * Created by wuxinyong on 15-4-14.
 */
public abstract class AbstractConfigManager implements IConfigManager {


    // 系统配置
    protected String serverConfigFile;


    /**
     * Bind IP Address
     */
    protected String host;

    /**
     * Server Port
     */
    protected int port;

    protected String commErrorPath;

    protected String statPath;

    protected ConcurrentHashMap<Integer, LogConfig> configMap = new ConcurrentHashMap<Integer, LogConfig>();

    protected ConcurrentHashMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    private void loadServerConfig(String serverConfigFile) throws Exception{
        if (!Strings.isNullOrEmpty(serverConfigFile)) {
            File file = new File(serverConfigFile);
            if (file.exists()) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(file));
                this.host = properties.getProperty(Constants.CONFIG_HOST);
                this.port = Integer.valueOf(properties
                        .getProperty(Constants.CONFIG_PORT));
                this.commErrorPath = properties
                        .getProperty(Constants.CONFIG_COMM_ERROR_PATH);
                this.statPath = properties.getProperty(Constants.CONFIG_STAT_PATH);
                loggerMap.put(this.commErrorPath, Utils.createLogger(this.commErrorPath,this.commErrorPath, Constants.DEFAULT_SPLIT));

            } else {
                throw new FileNotFoundException("server config file not found: " + serverConfigFile);
            }
        } else {
            throw new Exception("no server config file specified.");
        }
    }

    protected abstract void loadBussinessConfig() throws Exception;

    @Override
    public void load() throws Exception{
        // 1. load system config from input file path
        loadServerConfig(this.serverConfigFile);
        // 2. load bussiness config from localInput or zookeeper
        loadBussinessConfig();
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCommErrorPath() {
        return commErrorPath;
    }

    public void setCommErrorPath(String commErrorPath) {
        this.commErrorPath = commErrorPath;
    }

    public String getStatPath() {
        return statPath;
    }

    public void setStatPath(String statPath) {
        this.statPath = statPath;
    }

    public ConcurrentHashMap<Integer, LogConfig> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(ConcurrentHashMap<Integer, LogConfig> configMap) {
        this.configMap = configMap;
    }

    public String getServerConfigFile() {
        return serverConfigFile;
    }

    public void setServerConfigFile(String serverConfigFile) {
        this.serverConfigFile = serverConfigFile;
    }

    public ConcurrentHashMap<String, Logger> getLoggerMap() {
        return loggerMap;
    }

    public void setLoggerMap(ConcurrentHashMap<String, Logger> loggerMap) {
        this.loggerMap = loggerMap;
    }
}
