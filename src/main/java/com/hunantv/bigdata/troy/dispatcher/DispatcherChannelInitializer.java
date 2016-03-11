package com.hunantv.bigdata.troy.dispatcher;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LocalConfigManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/20
 * Time: PM6:40
 */
public class DispatcherChannelInitializer extends ChannelInitializer<SocketChannel> {

    private AbstractConfigManager configManager;

    public DispatcherChannelInitializer(AbstractConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("http-decoder", new HttpRequestDecoder());
        pipeline.addLast("http-aggregater", new HttpObjectAggregator(1048576));
        pipeline.addLast("http-encoder", new HttpResponseEncoder());
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("log-service", new DispatcherHandler(this.configManager));
    }
}
