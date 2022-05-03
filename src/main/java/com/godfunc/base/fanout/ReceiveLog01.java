package com.godfunc.base.fanout;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 将队列和fanout类型的交换机绑定，消费消息
 * @author godfunc
 */
public class ReceiveLog01 {


    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        //fanout 类型的交换机
        channel.exchangeDeclare(EmitLog.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //临时队列 会随机生成一个队列名
        String queueName = channel.queueDeclare().getQueue();

        // 绑定交换机和队列，路由key给空字符串
        channel.queueBind(queueName, EmitLog.EXCHANGE_NAME, "");
        System.out.println("c1等待接受消息");

        channel.basicConsume(queueName, true, new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                System.out.println("c1接受到的消息 " + new String(message.getBody(), StandardCharsets.UTF_8));
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("消息被取消 " + consumerTag);
            }
        });
    }
}
