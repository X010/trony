package com.hunantv.bigdata.troy.dispatcher;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by wuxinyong on 15-1-29.
 */
public class MonitorChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("http-decoder", new HttpRequestDecoder());
        pipeline.addLast("http-aggregater", new HttpObjectAggregator(65535));
        pipeline.addLast("http-encoder", new HttpResponseEncoder());
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("monitor", new MonitorHandler());
    }

}
