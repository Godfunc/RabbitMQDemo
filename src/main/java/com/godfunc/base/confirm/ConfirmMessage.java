package com.godfunc.base.confirm;


import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 确认消息发布
 */
public class ConfirmMessage {

    // 发布的消息条数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException {
        publishMessageAsync();
    }

    /**
     * 发一条消息确认一次
     */
    public static void publishMessageIndividually() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            // 交换机 空字符串 会使用默认交换机
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis(); // 耗时18878ms
        System.out.println("发布" + MESSAGE_COUNT + "单个确认，耗时" + (end - begin) + "ms");
    }

    /**
     * 发一批消息确认一次
     */
    public static void publishMessageBatch() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            if (i % 100 == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功");
                }
            }
        }
        long end = System.currentTimeMillis(); // 耗时383ms
        System.out.println("发布" + MESSAGE_COUNT + "单个确认，耗时" + (end - begin) + "ms");
    }

    /**
     * 异步确认
     */
    public static void publishMessageAsync() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        channel.addConfirmListener(new ConfirmCallback() {
            @Override
            public void handle(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("确认的消息" + deliveryTag);
            }
        }, new ConfirmCallback() {
            @Override
            public void handle(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("未确认的消息" + deliveryTag);
            }
        });
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        }
        long end = System.currentTimeMillis(); // 耗时383ms
        System.out.println("发布" + MESSAGE_COUNT + "单个确认，耗时" + (end - begin) + "ms");
    }
}
