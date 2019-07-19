package com.oceanier.core;

import com.oceanier.constant.Constants;
import com.oceanier.zk.ZookeeperFactory;
import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.nio.channels.Channel;
import java.util.HashSet;
import java.util.List;

public class ServerWatcher implements CuratorWatcher {

    public void process(WatchedEvent event) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();
//        String path = event.getPath();
        String path = Constants.SERVER_PATH;

        List<String> serverPaths = client.getChildren().forPath(path);
        client.getChildren().usingWatcher(this).forPath(path);

        ChannelManager.realServerPath.clear();

        for (String serverPath : serverPaths) {
            String[] str = serverPath.split("#");
            ChannelManager.realServerPath.add(str[0] + "#" + str[1]);
        }

        ChannelManager.clear();

        for (String realServer : ChannelManager.realServerPath) {
            String[] str = realServer.split("#");
            try {
                ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
                ChannelManager.add(channelFuture);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
