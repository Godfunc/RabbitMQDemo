package com.godfunc.base.priority;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author godfunc
 */
public class Producer {

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        for (int i = 0; i < 10; i++) {
            // 优先级高的先被消费，前提是消息都在队列中，进行优先级比对，
            // 如果消息一进来就被消费了，其他优先级比他高的再进来，这无法体现出优先级的作用
            String message = i + "";
            channel.basicPublish(Consumer.EXCHANGE_NAME, "k1",
                    new AMQP.BasicProperties().builder().priority(i).build(), message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
