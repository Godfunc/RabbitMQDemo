package com.godfunc.base.priority;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据队列中消息的优先级大小，决定消息的消费顺序
 */
public class Consumer {

    public static final String QUEUE_NAME = "pri_queue";
    public static final String EXCHANGE_NAME = "pri_exchange";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        Map<String, Object> arguments = new HashMap<>();
        // 设置队列的最大优先级值
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "k1");
        channel.basicConsume(QUEUE_NAME, true, new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                channel.basicCancel(consumerTag);
                System.out.println("接收到消息 "+ new String(message.getBody()));
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("取消到消息 "+ consumerTag);
            }
        });
    }
}
