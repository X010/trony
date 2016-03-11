package com.hunantv.bigdata.troy.parser;

import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.entity.Message;

/**
 * 日志解析接口
 * Created by wuxinyong on 15-1-22.
 */
public interface IParser extends Lifecycle{

    public Message parse(Message message);
}
