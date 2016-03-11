package com.hunantv.bigdata.troy.validate.validator;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.ValidatorConfig;
import com.hunantv.bigdata.troy.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by wuxinyong on 15-1-26.
 */
public class ConfigurationValidator extends ValidatorChain {

    private AbstractConfigManager configManager;

    public ConfigurationValidator(AbstractConfigManager configManager){
        this.configManager = configManager;
    }

    @Override
    public boolean check(Message message) {
        List<ValidatorConfig> validations = configManager.getConfigMap().get(message.getBid().hashCode()).getValidations();
        if(validations == null || validations.size() == 0){
            return true;
        }
        for(ValidatorConfig config : validations){
            Map<String, Object> keyValues = message.getKeyValues();
            if (config.getRequired() != null && config.getRequired()){
                Object valueObj = keyValues.get(config.getField());
                if(valueObj == null || "".equals(String.valueOf(valueObj).trim())){
                    return false;
                }
            }

            if(config.getValueReg() != null){
                String value = String.valueOf(keyValues.get(config.getField()));
                Pattern pattern = Pattern.compile(config.getValueReg());
                if(!pattern.matcher(value).matches()){
                    return false;
                }
            }
        }
        return true;
    }
}
