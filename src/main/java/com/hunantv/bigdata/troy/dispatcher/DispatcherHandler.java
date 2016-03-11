package com.hunantv.bigdata.troy.dispatcher;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.handler.AbstractHandler;
import com.hunantv.bigdata.troy.handler.DefaultHandler;
import com.hunantv.bigdata.troy.statistics.StatisticsUtils;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.Utils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/20
 * Time: PM6:41
 */
public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherHandler.class);

    private AbstractHandler handler;

    public DispatcherHandler(AbstractConfigManager configManager) {
        handler = new DefaultHandler(configManager);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (!fullHttpRequest.getDecoderResult().isSuccess()) {
            Utils.sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            LOG.warn("Decoding message failed.");
            return;
        }
        if (is100ContinueExpected(fullHttpRequest)) {
            channelHandlerContext.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = isKeepAlive(fullHttpRequest);

        // stat total logs
        StatisticsUtils.statTotal();
        // 对request进行处理
        Message message = handler.handle(channelHandlerContext, fullHttpRequest);

        HttpResponseStatus status;
        //根据返回的Message，生成相应的状态码返回客户端
        if (message.getStatus() == MessageStatus.BUILD_ERROR || message.getStatus() == MessageStatus.PARSE_ERROR
                || message.getStatus() == MessageStatus.VALID_ERROR) {
            status = HttpResponseStatus.BAD_REQUEST;
        } else {
            //返回客户端
            status = HttpResponseStatus.OK;

        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer("".getBytes()));
        response.headers().set(Constants.CONTENT_TYPE, "text/plain; Charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

//        channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);

        // open keep alive support
        if (!keepAlive) {
            channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            channelHandlerContext.write(response);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        LOG.error("Exception caught when HTTP communication.", cause);
        ctx.close();
    }


}
