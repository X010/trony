package com.hunantv.bigdata.troy.configure;

import com.google.common.base.Strings;
import com.hunantv.bigdata.troy.DirCreator;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.JsonUtils;
import com.hunantv.bigdata.troy.tools.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从zk加载业务配置文件
 *
 * Created by wuxinyong on 15-4-14.
 */
public class ZkConfigManager extends AbstractConfigManager implements IConfigChangeListener {

    private static final Logger LOG = Logger.getLogger(ZkConfigManager.class);

    private String zkAddress;
    private int zkTimeout;
    private String zkConfigRootNode;
    private String zkConfEncoding;

    private IConfigChangeSubscriber configSubscriber;

    private ZkClient zkClient;

    public ZkConfigManager(String serverConfigFile) {
        this.serverConfigFile = serverConfigFile;
    }

    @Override
    public void loadBussinessConfig() throws Exception {
        File file = new File(serverConfigFile);

        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        this.zkAddress = properties.getProperty(Constants.ZK_ADDRESS);
        this.zkTimeout = Integer.valueOf(properties
                    .getProperty(Constants.ZK_TIMEOUT));
        this.zkConfigRootNode = properties
                    .getProperty(Constants.ZK_CONFIG_ROOTNODE);
        this.zkConfEncoding = properties.getProperty(Constants.ZK_CONF_ENCODING);

        zkClient = new ZkClient(zkAddress, zkTimeout);
        zkClient.setZkSerializer(new ZkUtils.StringSerializer(zkConfEncoding));

        setInitValueAndSubscribe();

    }

    private void setInitValueAndSubscribe() throws Exception{
        configSubscriber = new ZkConfigChangeSubscriberImpl(zkClient, zkConfigRootNode);
        configSubscriber.subscribe(this);
        ConcurrentHashMap<String, String> initValues = configSubscriber.getConfigKeyValues();
        for(String key : initValues.keySet()){
            updateConfigMap(key, initValues.get(key));
        }

    }


    @Override
    public void configChanged(String path, String valueStr) {
        updateConfigMap(path, valueStr);
    }

    @Override
    public void configChanged() {
        try {
            if(configSubscriber != null){
                ConcurrentHashMap<String, String> initValues = configSubscriber.getConfigKeyValues();
                updateConfigMap(initValues);
            } else {
                LOG.error("Config subscriber is null.");
            }
            DirCreator.createDir(this);
        }catch (Exception e){
            LOG.error("Failed to update config.", e);
        }
    }


    private void updateConfigMap(String path, String valueStr){
        if (path.toString().indexOf("mapping") >= 0) {
            try {
                LogConfig logConfig = (LogConfig) JsonUtils
                        .StringToJsonVideo(valueStr,
                                LogConfig.class);
                if (logConfig != null) {
                    String bid = logConfig.getBid();

                    if (!Strings.isNullOrEmpty(bid)) {
                        configMap.put(bid.hashCode(), logConfig);
                    }
                }
            } catch (Exception e){
                LOG.error("Parse config error. config value: " + valueStr, e);
            }
        }
    }

    private void updateConfigMap(ConcurrentHashMap<String, String> initValues){
        synchronized (configMap) {
//            configMap.clear();
            for (String key : initValues.keySet()) {
                if (key.toString().indexOf("mapping") >= 0) {
                    try {
                        LogConfig logConfig = (LogConfig) JsonUtils
                                .StringToJsonVideo(initValues.get(key),
                                        LogConfig.class);
                        if (logConfig != null) {
                            String bid = logConfig.getBid();

                            if (!Strings.isNullOrEmpty(bid)) {
                                configMap.put(bid.hashCode(), logConfig);
                                LOG.info("Config updated for bid: " + bid + ", config value: " + initValues.get(key));
                            }
                        }
                    } catch (Exception e) {
                        LOG.error("Parse config error. config value: " + initValues.get(key), e);
                    }
                }
            }
        }
    }
}
