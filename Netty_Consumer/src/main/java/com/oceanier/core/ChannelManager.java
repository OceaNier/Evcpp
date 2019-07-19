package com.oceanier.core;

import io.netty.channel.ChannelFuture;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelManager {

    static Set<String> realServerPath = new HashSet<String>();

    static AtomicInteger position = new AtomicInteger(0);

    public static CopyOnWriteArrayList<ChannelFuture> channelFutures
            = new CopyOnWriteArrayList<ChannelFuture>();

    public static void removeChannel(ChannelFuture channel) {
        channelFutures.remove(channel);
    }

    public static void add(ChannelFuture channel) {
        channelFutures.add(channel);
    }

    public static void clear() {
        channelFutures.clear();
    }

    // 简单的负载均衡，可进行加权管理。对不同客户端请求分发至不同的服务器处理
    public static ChannelFuture get(AtomicInteger i) {
        int size = channelFutures.size();
        ChannelFuture channel = null;
        if (i.get() > size) {
            channel = channelFutures.get(0);
            ChannelManager.position = new AtomicInteger(1);
        } else {
            channel = channelFutures.get(i.getAndIncrement());
        }
        return channel;
    }
}
