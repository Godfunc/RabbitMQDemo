package com.godfunc.controller;

import com.godfunc.config.ConfirmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author godfunc
 */
@RestController
@RequestMapping("confirm")
public class ProducerController {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        // 如果交换机不可达，就会执行confirm失败回调
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + "1",
                ConfirmConfig.CONFIRM_ROUTING_KEY, message, new CorrelationData("1"));
        // 如果队列不可达，就会执行消息回退的回调
        /*rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY+ "1", message, new CorrelationData("1"));*/
        log.trace("发送消息内容 {}", message);
    }
}
