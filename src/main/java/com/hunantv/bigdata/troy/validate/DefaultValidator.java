package com.hunantv.bigdata.troy.validate;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LocalConfigManager;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.validate.validator.ConfigurationValidator;
import com.hunantv.bigdata.troy.validate.validator.NoneValidator;
import com.hunantv.bigdata.troy.validate.validator.ValidatorChain;

/**
 * 默认的校验实现类
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/21
 * Time: AM9:23
 */
public class DefaultValidator implements IValidator {

    private AbstractConfigManager configManager;

    public DefaultValidator(AbstractConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * 校验日志，根据配置文件校验
     * @param message
     * @return
     */
    @Override
    public Message validate(Message message) {
        ValidatorChain validator = new NoneValidator();
        //对批量日志上报暂不做校验
        if(!message.getIsBatch()) {
            validator.setNextValidator(new ConfigurationValidator(configManager));
        }
        if(validator.validate(message)){
            message.setStatus(MessageStatus.VALID_SUCC);
        } else {
            message.setStatus(MessageStatus.VALID_ERROR);
        }
        return message;
    }

    @Override
    public void doStart() {

    }

    @Override
    public void destory() {

    }
}
