package com.hunantv.bigdata.troy.handler;

import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LocalConfigManager;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.statistics.StatisticsUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 请求处理模版类，每条请求按照如下流程进行处理：
 * 1. 根据request创建Message
 * 2. 格式化Message
 * 3. 校验Message
 * 4. 输出Message
 * 5. 返回Message供dispatcher参考返回相应的HTTP状态码
 * Created by wuxinyong on 15-1-22.
 */
public abstract class AbstractHandler implements Lifecycle {

    private AbstractConfigManager configManager;

    public AbstractHandler(){
    }

    public AbstractHandler(AbstractConfigManager configManager){
        this.configManager = configManager;
    }

    /**
     * 根据request创建Message对象
     * @param ctx
     * @param request
     * @return
     */
    public abstract Message build(ChannelHandlerContext ctx, FullHttpRequest request);

    /**
     * 解析Message的请求内容，生成Map
     * @param message
     * @return
     */
    public abstract  Message parse(Message message);

    /**
     * 校验Message对象的request内容
     * @param message
     * @return
     */
    public abstract Message validate(Message message);

    /**
     * 格式化Message对象的request内容 便于输出
     * @param message
     * @return
     */
    public abstract Message format(Message message);

    /**
     * 输出Message对象的request内容
     * @param message
     * @return
     */
    public abstract Message output(Message message);

    /**
     * 请求处理的模版方法
     * @param ctx
     * @param request
     * @return
     */
    public Message handle(ChannelHandlerContext ctx,  FullHttpRequest request) {
        Message message = build(ctx, request);
        if(message.getStatus() != MessageStatus.BUILD_ERROR) {
            parse(message);
            if(message.getStatus() != MessageStatus.PARSE_ERROR && message.getStatus() != MessageStatus.IS_DEBUG) {
                validate(message);
            }
        }
        format(message);
        output(message);
        //统计日志
        StatisticsUtils.stat(message);
        return message;
    }

    @Override
    public void doStart() {

    }

    @Override
    public void destory() {

    }
}
