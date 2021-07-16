package com.jinzl.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * curator客户端实例工厂
 *
 * @author Zhenlin Jin
 * @date 2021/7/16 10:03
 */
public class ClientFactory {
    public static CuratorFramework createSimple(String connectionString) {
        // 重试策略：第一次重试等待1秒，第二次重试等待2秒，第三次重试等待4秒
        // 第一个参数：等待时间的基础单位，单位为毫秒
        // 第二个参数：最大重试次数
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        // 获取CuratorFramework实例的最简单方式
        // 第一个参数：zk的连接地址
        // 第二个参数：重试策略
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }

    public static CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy,
                                                     int connectionTimeoutMs, int sessionTimeoutMs) {
        // 用builder方法创建CuratorFramework实例
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

}
