package com.hunantv.bigdata.troy.configure;

import com.hunantv.bigdata.troy.tools.ZkUtils;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zk 配置改变回调类
 *
 * Created by wuxinyong on 15-4-13.
 */
public class ZkConfigChangeSubscriberImpl implements IConfigChangeSubscriber {

    private ZkClient zkClient;
    private String rootNode;

    private ConcurrentHashMap<String, String> configKeyValues = new ConcurrentHashMap<String, String>();

    public ZkConfigChangeSubscriberImpl(ZkClient zkClient, String rootNode) {
        this.rootNode = rootNode;
        this.zkClient = zkClient;
    }

    @Override
    public void subscribe(IConfigChangeListener listener) {
        if (!this.zkClient.exists(rootNode)) {
            throw new RuntimeException(
                    "配置("
                            + rootNode
                            + ")不存在, 必须先定义配置才能监听配置的变化, 请检查配置的path是否正确, 如果确认配置path正确, 那么需要保证先使用配置发布命令发布配置! ");
        }

        ChildListenerAdapter adapter = new ChildListenerAdapter(listener);
        DataListenerAdapter dataAdapter = new DataListenerAdapter(listener);

        this.zkClient.subscribeChildChanges(rootNode, adapter);
        for(String key : this.listKeys()){
            this.zkClient.subscribeDataChanges(ZkUtils.getZkPath(rootNode, key), dataAdapter);
        }
    }

    public ConcurrentHashMap<String, String>  getConfigKeyValues() {
        List<String> keys = this.listKeys();

        configKeyValues.clear();
        for(String key : keys){
            String initValue = zkClient.readData(ZkUtils.getZkPath(rootNode, key));
            configKeyValues.put(key, initValue);
        }
        return configKeyValues;
    }

    public String getInitValue(String key) {
        String path = ZkUtils.getZkPath(this.rootNode, key);
        return (String) this.zkClient.readData(path);
    }

    public List<String> listKeys() {
        return this.zkClient.getChildren(this.rootNode);
    }

    /**
     * 子结点监听器适配类，当子结点变化时，触发IConfigChangeListener
     *
     */
    private class ChildListenerAdapter implements IZkChildListener {
        private IConfigChangeListener configListener;
        public ChildListenerAdapter(IConfigChangeListener configListener){
            this.configListener = configListener;
        }

        @Override
        public void handleChildChange(String s, List<String> list) throws Exception {
            configListener.configChanged();
        }
    }

    /**
     * 数据监听器适配类，当zk数据变化时，触发IConfigChangeListener
     *
     */
    private class DataListenerAdapter implements IZkDataListener {
        private IConfigChangeListener configListener;
        public DataListenerAdapter(IConfigChangeListener configListener){
            this.configListener = configListener;
        }

        @Override
        public void handleDataChange(String s, Object o) throws Exception {
            configListener.configChanged();
        }

        @Override
        public void handleDataDeleted(String s) throws Exception {

        }
    }
}
