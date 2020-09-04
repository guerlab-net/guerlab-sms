package net.guerlab.sms.baiducloud;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 百度云发送端点自动配置
 *
 * @author guer
 */
@Configuration
@EnableConfigurationProperties(BaiduCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class BaiduCloudAutoConfigure {

    /**
     * 构造百度云发送处理
     *
     * @param properties
     *         配置对象
     * @param loadbalancer
     *         负载均衡器
     * @return 百度云发送处理
     */
    @Bean
    @Conditional(BaiduCloudSendHandlerCondition.class)
    public BaiduCloudSendHandler baiduCloudSendHandler(BaiduCloudProperties properties,
            SmsSenderLoadBalancer loadbalancer) {
        BaiduCloudSendHandler handler = new BaiduCloudSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        return handler;
    }

    public static class BaiduCloudSendHandlerCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.baiducloud.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
