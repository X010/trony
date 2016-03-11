package com.hunantv.bigdata.troy.validate.validator;

import com.hunantv.bigdata.troy.entity.Message;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/21
 * Time: AM9:24
 */
public abstract class ValidatorChain {

    /**
     * 下一个验证策略
     */
    private ValidatorChain nextValidator;

    /**
     * 验证
     *
     * @param message
     */
    public abstract boolean check(Message message);

    /**
     * 校验模版方法
     * @param message
     * @return
     */
    public boolean validate(Message message) {
        if(check(message)){
            if(nextValidator != null){
                return nextValidator.check(message);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public ValidatorChain getNextValidator() {
        return nextValidator;
    }

    public void setNextValidator(ValidatorChain nextValidator) {
        this.nextValidator = nextValidator;
    }
}
