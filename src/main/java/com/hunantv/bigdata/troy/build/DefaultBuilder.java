package com.hunantv.bigdata.troy.build;

import com.google.common.base.Strings;
import com.hunantv.bigdata.troy.entity.Message;
import com.hunantv.bigdata.troy.entity.MessageStatus;
import com.hunantv.bigdata.troy.tools.Constants;
import com.hunantv.bigdata.troy.tools.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/1/21
 * Time: AM9:21
 */
public class DefaultBuilder implements IBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultBuilder.class);

    public Message build(ChannelHandlerContext ctx, FullHttpRequest request){
        Message message = new Message();
        message.setTime(Utils.getCurrentTime());
        String ip = request.headers().get("X-Forwarded-For");
        // if there is no proxy
        if (Strings.isNullOrEmpty(ip)) {
            InetSocketAddress inSocket = (InetSocketAddress) ctx.channel()
                    .remoteAddress();
            ip = inSocket.getAddress().getHostAddress();
        }
        message.setIp(ip);
        message.setAccept(request.headers().get(Constants.HTTP_HEADER_ACCEPT));
        message.setAcceptEncoding(request.headers().get(Constants.HTTP_HEADER_ACCEPT_ENCODING));
        message.setAcceptLanguage(request.headers().get(Constants.HTTP_HEADER_ACCEPT_LANGUAGE));
        message.setConnection(request.headers().get(Constants.HTTP_HEADER_CONNECTION));
        message.setCookie(request.headers().get(Constants.HTTP_HEADER_COOKIE));
        message.setHost(request.headers().get(Constants.HTTP_HEADER_HOST));
        message.setUa(request.headers().get(Constants.HTTP_HEADER_UA));
        message.setReferer(request.headers().get(Constants.HTTP_HEADER_REFERER));
        message.setMethod(request.getMethod());
        message.setContentType(request.headers().get(Constants.HTTP_HEADER_CONTENT_TYPE));
        message.setBid(Constants.INVALID_BID);
        String batch = request.headers().get(Constants.HTTP_HEADER_BATCH);

        if(batch != null && Constants.HTTP_HEADER_BATCH_VALUE.equals(batch)){
            message.setIsBatch(Boolean.TRUE);
        }
        setRawLog(request, message);
        return message;
    }

    private void  setRawLog(FullHttpRequest request, Message message) {
        if (request.getMethod() == HttpMethod.GET) {
            String url = request.getUri();
            if (url.indexOf("?") >= 0) {
                message.setRawLog(url.substring(url.indexOf("?") + 1, url.length()));
                message.setStatus(MessageStatus.BUILD_SUCC);
            } else {
                message.setRawLog(url);
                message.setStatus(MessageStatus.BUILD_ERROR);
                message.setErrorInfo("Http get URL is invalid.");
            }
        } else if (request.getMethod() == HttpMethod.POST){
            ByteBuf buf = request.content();
            if(buf != null){
                message.setRawLog(buf.toString(Charset.forName("UTF-8")));
                message.setStatus(MessageStatus.BUILD_SUCC);
            } else {
                message.setStatus(MessageStatus.BUILD_ERROR);
                message.setErrorInfo("Http post content is null.");
            }
        } else {
            message.setStatus(MessageStatus.BUILD_ERROR);
            message.setErrorInfo("Http method not supported. method = " + request.getMethod());
        }
    }


    @Override
    public void doStart() {

    }

    @Override
    public void destory() {

    }
}
