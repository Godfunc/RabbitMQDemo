package com.godfunc.base.dead;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

/**
 * @author godfunc
 */
public class Consumer2 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.basicConsume(NORMAL_QUEUE, false, new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody());
                if ("info5".equals(msg)) {
                    // 拒绝需要配合手动应答才生效
                    channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                } else {
                    System.out.println("normal消息 " + msg);
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                }
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        });
    }
}
