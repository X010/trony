package com.hunantv.bigdata.troy.build;

import com.hunantv.bigdata.troy.Lifecycle;
import com.hunantv.bigdata.troy.entity.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 日志解析,创建Message对象
 * Created by wuxinyong on 15-1-22.
 */
public interface IBuilder extends Lifecycle {

    /**
     * 对Message中封装的日志进行解析
     *
     * @return
     */
    public Message build(ChannelHandlerContext ctx, FullHttpRequest request);
}
