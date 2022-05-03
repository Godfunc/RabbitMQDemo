package com.godfunc.base.four;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 通过异步确认，将失败的消息保存到 outstandingConfirms 中
 * 这个方案有问题，在持续消息发送的过程中，无法确定在队列中的消息的状态
 */
public class ConfirmMessage {

    public static void main(String[] args) throws IOException {
        async();
    }

    public static void async() throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启消息确认
        channel.confirmSelect();

        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 添加消息确认回调
        channel.addConfirmListener((deliveryTag, multiple) -> {
            // 确认成功的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                // 单个确认的情况下，移除当前确认成功的消息
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息 " + deliveryTag);
        }, (deliveryTag, multiple) -> {
            // 未确认消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息 " + deliveryTag +  " 未确认消息" + message);
        });

        long begin = System.currentTimeMillis();
        // 循环发送消息
        for (int i = 0; i < 10000; i++) {
            String message = "消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            // 将发送的消息保存到集合中
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        System.out.println("耗时 " + (System.currentTimeMillis() - begin));
    }
}
