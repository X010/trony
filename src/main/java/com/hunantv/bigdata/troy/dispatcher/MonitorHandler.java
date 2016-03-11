package com.hunantv.bigdata.troy.dispatcher;

import com.google.common.base.Strings;
import com.hunantv.bigdata.troy.statistics.StatisticsUtils;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.Utils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by wuxinyong on 15-1-29.
 */
public class MonitorHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOG = Logger.getLogger(MonitorHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        if (!request.getDecoderResult().isSuccess()) {
            Utils.sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (is100ContinueExpected(request)) {
            channelHandlerContext.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = isKeepAlive(request);
        String url = request.getUri();
        DefaultFullHttpResponse response;
        if (url.indexOf("metrics") > 0) {
            String stats = StatisticsUtils.getStats();
            if(!Strings.isNullOrEmpty(stats)) {
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(stats.getBytes()));
                response.headers().set(Constants.CONTENT_TYPE, "application/json; Charset=UTF-8");
            }else{
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer("Server Error...".getBytes()));
                response.headers().set(Constants.CONTENT_TYPE, "text/plain; Charset=UTF-8");
            }

        } else {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("404 Not Found...".getBytes()));
            response.headers().set(Constants.CONTENT_TYPE, "text/plain; Charset=UTF-8");
        }
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("Exception caught when HTTP communication.", cause);
        ctx.close();
    }
}
