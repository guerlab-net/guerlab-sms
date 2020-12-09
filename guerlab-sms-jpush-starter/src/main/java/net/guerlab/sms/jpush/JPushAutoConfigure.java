package net.guerlab.sms.jpush;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 极光发送端点自动配置
 *
 * @author guer
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Configuration
@EnableConfigurationProperties(JPushProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class JPushAutoConfigure {

    /**
     * 构造极光发送处理
     *
     * @param properties
     *         配置对象
     * @param loadbalancer
     *         负载均衡器
     * @param objectMapper
     *         objectMapper
     * @return 极光发送处理
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Conditional(JPushSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    public JPushSendHandler qiNiuSendHandler(JPushProperties properties, SmsSenderLoadBalancer loadbalancer,
            ObjectMapper objectMapper) {
        JPushSendHandler handler = new JPushSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class JPushSendHandlerCondition implements Condition {

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.jpush.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
