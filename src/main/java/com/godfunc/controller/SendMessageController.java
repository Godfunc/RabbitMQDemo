package com.godfunc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author godfunc
 */
@RestController
@RequestMapping("ttl")
public class dSendMessageController {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间{}，发送一条消息给两个TTL队列{}", new Date().toString(), message);
        // XA 对应的队列设置了ttl过期时间
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列" + message);
    }

    @GetMapping("sendExpMsg/{message}/{ttl}")
    public void sendExpMsg(@PathVariable String message, @PathVariable String ttl) {
        log.info("当前时间{}，发送一条时长 {} 消息给两个TTL队列{}", new Date().toString(), ttl, message);
        rabbitTemplate.convertAndSend("X", "XC", "消息来自ttl为10s的队列" + message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 给消息设置过期时间
                message.getMessageProperties().setExpiration(ttl);
                return message;
            }
        });
    }
}
