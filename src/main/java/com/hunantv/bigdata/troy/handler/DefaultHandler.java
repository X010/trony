package com.hunantv.bigdata.troy.handler;

import com.hunantv.bigdata.troy.build.DefaultBuilder;
import com.hunantv.bigdata.troy.build.IBuilder;
import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LocalConfigManager;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.format.DefaultFormatter;
import com.hunantv.bigdata.troy.format.IFormatter;
import com.hunantv.bigdata.troy.output.DefaultOutPut;
import com.hunantv.bigdata.troy.output.IOutput;
import com.hunantv.bigdata.troy.parser.DefaultParser;
import com.hunantv.bigdata.troy.parser.IParser;
import com.hunantv.bigdata.troy.validate.DefaultValidator;
import com.hunantv.bigdata.troy.validate.IValidator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 默认的请求处理类
 * Created by wuxinyong on 15-1-22.
 */
public class DefaultHandler extends AbstractHandler {
    private IBuilder builder;
    private IParser parser;
    protected IValidator validator;
    private IFormatter formatter;
    private IOutput outputer;

    public DefaultHandler(AbstractConfigManager configManager) {
        super(configManager);
        this.builder = new DefaultBuilder();
        this.parser = new DefaultParser(configManager);
        this.validator = new DefaultValidator(configManager);
        this.formatter = new DefaultFormatter(configManager);
        this.outputer = new DefaultOutPut(configManager);
    }

    public DefaultHandler(IBuilder builder, IParser parser, IValidator validator, IFormatter formatter, IOutput outputer) {
        this.builder = builder;
        this.parser = parser;
        this.validator = validator;
        this.formatter = formatter;
        this.outputer = outputer;
    }


    @Override
    public Message build(ChannelHandlerContext ctx, FullHttpRequest request) {
        return builder.build(ctx, request);
    }

    @Override
    public Message parse(Message message){
        return parser.parse(message);
    }

    @Override
    public Message validate(Message message) {
        return validator.validate(message);
    }

    @Override
    public Message format(Message message) {
        return formatter.format(message);
    }

    @Override
    public Message output(Message message) {
        outputer.output(message.getStatus(), message.getBid(), message.getFlow(),message.getFormatLog());
        return message;
    }
}
