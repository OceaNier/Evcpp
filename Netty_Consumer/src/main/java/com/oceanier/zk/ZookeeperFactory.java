package com.oceanier.zk;

import com.oceanier.constant.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperFactory {

    public static CuratorFramework client;

    public static CuratorFramework create() {
        if (client == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient("10.211.55.5:2181", retryPolicy);
            client.start();
        }
        return client;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = create();
        client.create().forPath(Constants.SERVER_PATH);
    }
}
