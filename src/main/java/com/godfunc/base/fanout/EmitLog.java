package com.godfunc.base.fanout;

import com.godfunc.base.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author godfunc
 * fanout类型的交换机，因为fanout类型的交换机会将消息广播给所有绑定的队列，所以路由key给空字符串就行
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            System.out.println("发送消息 " + message);
            // fanout类型的交换机 routingKey给空字符串
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        }

    }
}
