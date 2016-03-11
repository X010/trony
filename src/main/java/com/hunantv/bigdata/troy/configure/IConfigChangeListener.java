package com.hunantv.bigdata.troy.configure;

/**
 * Created by wuxinyong on 15-4-13.
 */
public interface IConfigChangeListener {
    public void configChanged(String key, String newVlaue);
    public void configChanged();
}
