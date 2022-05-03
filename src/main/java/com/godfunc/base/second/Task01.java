package com.godfunc.base.second;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 控制台输入消息并发布
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息 消息是轮训发送给每个消费者的
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            channel.basicPublish("", QUEUE_NAME, null, scanner.next().getBytes(StandardCharsets.UTF_8));
        }
    }
}
