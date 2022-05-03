package com.godfunc.base.dead;

import com.godfunc.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author godfunc
 */
public class Producer {

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        // 过期时间
        // AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(com.godfunc.eight.Consumer1.NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
