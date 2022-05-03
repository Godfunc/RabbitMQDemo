package com.godfunc.base.first;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("1.13.254.128");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 收到消息会执行的方法
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        // 取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息被中断");
        };
        // 队列名 是否自动应答 收到消息的回调 取消消息的回调
        channel.basicConsume(Producer.QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
