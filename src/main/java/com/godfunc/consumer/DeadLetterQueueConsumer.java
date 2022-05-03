package com.godfunc.consumer;

import com.godfunc.config.TtlQueueConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 接收死信消息
 */
@Component
public class DeadLetterQueueConsumer {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = TtlQueueConfig.DEAD_LETTER_QUEUE)
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间{}，收到死信队列的消息 {}",new Date().toString(), msg);
    }
}
