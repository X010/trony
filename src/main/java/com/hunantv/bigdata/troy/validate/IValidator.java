package com.hunantv.bigdata.troy.validate;

import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.entity.Message;

/**
 * 日志校验接口
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/20
 * Time: PM7:30
 * 数据合法性验证
 */
public interface IValidator extends Lifecycle {

    /**
     * 验证Meesgae
     *
     * @param message
     * @return
     */
    public Message validate(Message message);
}
