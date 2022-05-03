package com.godfunc.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author godfunc
 */
@Configuration
public class TtlQueueConfig {

    // 普通交换机的名称
    public static final String X_EXCHANGE = "X";
    // 死信交换机的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    // 普通队列的名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    // 死信队列的名称
    public static final String DEAD_LETTER_QUEUE = "QD";

    // 普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }
    // 死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    // ttl 10s 的普通队列，超时会进入死信队列
    @Bean("queueA")
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A)
                .withArgument("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "YD")
                // 设置 ttl 单位 ms
                .withArgument("x-message-ttl", 10000)
                .build();
    }
    // ttl 40s 的普通队列，超时会进入死信队列
    @Bean("queueB")
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
                .withArgument("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "YD")
                // 设置 ttl 单位 ms
                .withArgument("x-message-ttl", 40000)
                .build();
    }

    /**
     * 为队列 QC 绑定死信交换机和路由key
     * @return
     */
    @Bean("queueC")
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
                .withArgument("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "YD")
                .build();
    }
    // 死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Binding queueABinding(@Qualifier("queueA") Queue queueA,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueBinding(@Qualifier("queueB") Queue queueB,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
    @Bean
    public Binding queueDBinding(@Qualifier("queueD") Queue queueD,
                                @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
