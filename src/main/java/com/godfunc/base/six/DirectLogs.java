package com.godfunc.base.six;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author godfunc
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String[] split = scanner.next().split(",");
            String message = split[1];
            channel.basicPublish(EXCHANGE_NAME, split[0], null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息 " + message);
        }
    }
}
