package com.godfunc.base.six;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author godfunc
 */
public class ReceiveLog01 {

    public static final String QUEUE_NAME = "disk";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        //fanout 类型的交换机
        channel.exchangeDeclare(DirectLogs.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //临时队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定交换机和队列
        channel.queueBind(QUEUE_NAME, DirectLogs.EXCHANGE_NAME, "error");
        System.out.println("c1等待接受消息");

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
