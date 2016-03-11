package com.hunantv.bigdata.troy.configure;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxinyong on 15-4-13.
 */
public interface IConfigChangeSubscriber {
    public abstract String getInitValue(String paramString);

    public ConcurrentHashMap<String, String> getConfigKeyValues();

    public void subscribe(IConfigChangeListener paramConfigChangeListener);

    public List<String> listKeys();
}
