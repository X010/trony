package com.hunantv.bigdata.troy.format;

import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.entity.Message;

/**
 * 日志格式化接口
 * Created by wuxinyong on 15-1-22.
 */
public interface IFormatter extends Lifecycle{

    public Message format(Message message);
}
