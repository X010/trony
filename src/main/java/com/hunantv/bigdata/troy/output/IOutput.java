package com.hunantv.bigdata.troy.output;

import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;

/**
 * Created by wuxinyong on 15-1-22.
 */
public interface IOutput extends Lifecycle {
	/**
     * @param messageStatus 日志类型 VALID_SUCC, VALID_FAIL, PARSE_FAIL, IS_DEBUG, NO_BID
     * @param bid 业务ID
     * @param content 内容 格式化后的  string 文本
     * @return 成功失败 true false 
     */
    public boolean output(MessageStatus messageStatus, String bId, String flow, String content);

    public Message output(Message message);
    
}
