package com.hunantv.bigdata.troy;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/20
 * Time: PM7:19
 * 生命周期管理
 */
public interface Lifecycle {

    /**
     * 启动组件
     */
    public void doStart();

    /**
     * 结束组件
     */
    public void destory();
}
