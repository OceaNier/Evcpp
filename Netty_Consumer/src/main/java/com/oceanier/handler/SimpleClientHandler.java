package com.oceanier.handler;

import com.alibaba.fastjson.JSONObject;
import com.oceanier.core.DefaultFuture;
import com.oceanier.handler.param.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if ("ping".equals(msg)) {
//            ctx.channel().writeAndFlush("ping\r\n");
//            return;
//        }
//        System.out.println(msg.toString());
//        ctx.channel().attr(AttributeKey.valueOf("message")).set(msg);
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        System.out.println("接受服务器返回数据" + JSONObject.toJSONString(response));
        DefaultFuture.receive(response);
//        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
