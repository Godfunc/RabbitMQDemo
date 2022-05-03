package com.godfunc.base.second;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * 启多个消费者
 * 消息是轮训发送给每个消费者的
 */
public class Work01 {
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        // 收到消息会执行的方法
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        // 取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息被中断");
        };
        System.out.println("c3等待接收消息");
        channel.basicConsume(Task01.QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
