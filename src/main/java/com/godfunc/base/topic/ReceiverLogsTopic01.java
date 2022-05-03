package com.godfunc.base.topic;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author godfunc
 */
public class ReceiverLogsTopic01 {

    public static final String QUEUE_NAME = "Q1";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(TopicLogs.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 设置路由key
        channel.queueBind(QUEUE_NAME, TopicLogs.EXCHANGE_NAME, "*.orange.*", null);
        channel.basicConsume(QUEUE_NAME, true, new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                System.out.println("c1接受到的消息 " + new String(message.getBody(), StandardCharsets.UTF_8));
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("消息被取消");
            }
        });
    }
}
