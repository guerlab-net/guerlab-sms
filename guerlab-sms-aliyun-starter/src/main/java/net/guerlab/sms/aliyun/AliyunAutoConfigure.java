package net.guerlab.sms.aliyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 阿里云发送端点自动配置
 *
 * @author guer
 *
 */
@Configuration
@EnableConfigurationProperties(AliyunProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class AliyunAutoConfigure {

    /**
     * 构造阿里云发送处理
     *
     * @param properties
     *         配置对象
     * @param objectMapper
     *         objectMapper
     * @param loadbalancer
     *         负载均衡器
     * @return 阿里云发送处理
     */
    @Bean
    @Conditional(AliyunSendHandlerCondition.class)
    public AliyunSendHandler aliyunSendHandler(AliyunProperties properties, ObjectMapper objectMapper,
            SmsSenderLoadBalancer loadbalancer) {
        AliyunSendHandler handler = new AliyunSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        return handler;
    }

    public static class AliyunSendHandlerCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.aliyun.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
