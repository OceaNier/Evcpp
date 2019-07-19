package com.oceanier.core;

import com.alibaba.fastjson.JSONObject;
import com.oceanier.constant.Constants;
import com.oceanier.handler.SimpleClientHandler;
import com.oceanier.handler.param.Response;
import com.oceanier.zk.ZookeeperFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.GetChildrenBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TcpClient {

    static final Bootstrap b = new Bootstrap();
    static ChannelFuture f = null;

    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                        65535, Delimiters.lineDelimiter()[0]));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new SimpleClientHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });

        CuratorFramework client = ZookeeperFactory.create();

        String host = "localhost";
        int port = 8080;

        try {
            // 获取子节点，去重。监听服务器状态，宕机时发现
            List<String> serverPaths = client.getChildren().forPath(Constants.SERVER_PATH);

            CuratorWatcher watcher = new ServerWatcher();
            // 加上zk监听服务器变化。监听路径下服务器变化
            client.getChildren().usingWatcher(watcher).forPath(Constants.SERVER_PATH);

            for (String serverPath : serverPaths) {
                String[] str = serverPath.split("#");
                ChannelManager.realServerPath.add(str[0] + "#" + str[1]);

                ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
                ChannelManager.add(channelFuture);
            }

            if (ChannelManager.realServerPath.size() > 0) {
                String[] hostAndPort = ChannelManager.realServerPath.toArray()[0].toString().split("#");
                host = hostAndPort[0];
                port = Integer.valueOf(hostAndPort[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        try {
//            f = b.connect(host, port).sync(); // (5)
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 注意：1.每个请求都是同一个链接，并发问题。
     * request
     * 1）唯一ID识别
     * 2）请求内容
     * <p>
     * response
     * 1）响应唯一识别码id
     * 2）响应结果
     */
    //发送数据 长链接
    public static Response send(ClientRequest request) {
        f = ChannelManager.get(ChannelManager.position);
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        DefaultFuture df = new DefaultFuture(request);

        return df.get();
    }
}
