package com.jinzl.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 使用Curator操作zookeeper
 *
 * @author Zhenlin Jin
 * @date 2021/7/16 10:13
 */
public class CuratorTest {
    /**
     * 创建节点
     */
    @Test
    public void testCreateNode() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple("127.0.0.1:2181");

        try {
            // 启动客户端实例，连接服务器
            client.start();

            // 创建第一个ZNode节点
            // 节点的数据为payload
            String data = "hello,zookeeper";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 读取节点
     */
    @Test
    public void testReadNode() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple("127.0.0.1:2181");

        try {
            // 启动客户端实例，连接服务器
            client.start();

            String zkPath = "/test/CRUD/node-1";

            Stat stat = client.checkExists().forPath(zkPath);
            if (stat != null) {
                // 读取节点的数据
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                System.out.println("read data:" + data);

                String parentPath = "/test";
                List<String> children = client.getChildren().forPath(parentPath);
                for (String child : children) {
                    System.out.println("child:" + child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 同步更新
     */
    @Test
    public void testUpdateNodeSync() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple("127.0.0.1:2181");

        try {
            // 启动客户端实例，连接服务器
            client.start();

            String zkPath = "/test/CRUD/node-1";

            String data = "hello world";
            byte[] payload = data.getBytes("UTF-8");

            client.setData()
                    .forPath(zkPath, payload);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testUpdateNodeAsync() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple("127.0.0.1:2181");

        try {
            // 异步更新完成，回调此实例
            AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
                // 回调方法
                @Override
                public void processResult(int i, String s, Object o, String s1) {
                    System.out.println(
                            "i = " + i + " | " +
                            "s = " + s + " | " +
                            "o = " + o + " | " +
                            "s1 = " + s1

                    );
                }
            };

            // 启动客户端实例，连接服务器
            client.start();

            String zkPath = "/test/CRUD/node-1";

            String data = "hello,every body!!!!";
            byte[] payload = data.getBytes("UTF-8");

            client.setData()
                    .inBackground(callback)
                    .forPath(zkPath, payload);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testDelete() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple("127.0.0.1:2181");

        try {
            // 启动客户端实例，连接服务器
            client.start();

            String zkPath = "/test/CRUD/node-1";

            client.delete()
                    .forPath(zkPath);

            // 删除后查看结果
            String parentPath = "/test";
            List<String> children = client.getChildren().forPath(parentPath);
            for (String child : children) {
                System.out.println("child:" + child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
