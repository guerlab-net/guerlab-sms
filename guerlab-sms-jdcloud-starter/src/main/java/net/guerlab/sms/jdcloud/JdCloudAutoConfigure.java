package net.guerlab.sms.jdcloud;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 京东云发送端点自动配置
 *
 * @author guer
 */
@Configuration
@EnableConfigurationProperties(JdCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class JdCloudAutoConfigure {

    /**
     * 构造京东云发送处理
     *
     * @param properties
     *         配置对象
     * @param loadbalancer
     *         负载均衡器
     * @return 京东云发送处理
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Conditional(JdCloudSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    public JdCloudSendHandler jdCloudSendHandler(JdCloudProperties properties, SmsSenderLoadBalancer loadbalancer) {
        JdCloudSendHandler handler = new JdCloudSendHandler(properties);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class JdCloudSendHandlerCondition implements Condition {

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.jdcloud.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
