package net.guerlab.sms.huaweicloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 华为云发送端点自动配置
 *
 * @author guer
 */
@Configuration
@EnableConfigurationProperties(HuaWeiCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class HuaWeiCloudAutoConfigure {

    /**
     * 构造华为云发送处理
     *
     * @param properties
     *         配置对象
     * @param objectMapper
     *         objectMapper
     * @param loadbalancer
     *         负载均衡器
     * @return 华为云发送处理
     */
    @Bean
    @Conditional(HuaWeiCloudSendHandlerCondition.class)
    public HuaWeiCloudSendHandler huaWeiCloudSendHandler(HuaWeiCloudProperties properties, ObjectMapper objectMapper,
            SmsSenderLoadBalancer loadbalancer) {
        HuaWeiCloudSendHandler handler = new HuaWeiCloudSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        return handler;
    }

    public static class HuaWeiCloudSendHandlerCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.huawei.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
