package com.godfunc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 确认回调 和 消息退回回调
 */
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 开启publisher-confirm-type=correlated
     * 交换机确认回调方法
     * ack = true 交换机接收到了消息
     * ack = false 交换机没有接收到消息 cause 没有接收到消息的原因
     *
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到id为 {} 的消息", id);
        } else {
            log.info("交换机还未收到id为 {} 的消息，原因 {}", id, cause);
        }
    }

    /**
     * 如果开启了 mandatory = true
     * 如果消息由交换机发送给队列的过程中失败了（比如路由写错了），就会触发回调消息的回调
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息 {}被交换机给回退了，退回的原因 {}，路由key {}", new String(returned.getMessage().getBody()), returned.getExchange(), returned.getRoutingKey());
    }
}
