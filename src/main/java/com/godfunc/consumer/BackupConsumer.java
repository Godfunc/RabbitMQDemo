package com.godfunc.consumer;

import com.godfunc.config.ConfirmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费备份队列中的消息
 */
@Component
public class BackupConsumer {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = ConfirmConfig.BACKUP_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("backup接收到消息 {}", msg);
    }
}
