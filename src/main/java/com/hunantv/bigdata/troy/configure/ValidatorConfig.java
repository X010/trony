package com.hunantv.bigdata.troy.configure;

/**
 * Created by wuxinyong on 15-1-26.
 */
public class ValidatorConfig {
    private String field;
    private Boolean required;
    private String valueReg;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getValueReg() {
        return valueReg;
    }

    public void setValueReg(String valueReg) {
        this.valueReg = valueReg;
    }
}
