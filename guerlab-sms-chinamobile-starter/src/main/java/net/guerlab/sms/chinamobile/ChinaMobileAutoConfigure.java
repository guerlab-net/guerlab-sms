/*
 * Copyright 2018-2021 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.guerlab.sms.chinamobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 移动云发送端点自动配置
 *
 * @author guer
 */
@Configuration
@EnableConfigurationProperties(ChinaMobileProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class ChinaMobileAutoConfigure {

    /**
     * 构造移动云发送处理
     *
     * @param properties
     *         配置对象
     * @param objectMapper
     *         objectMapper
     * @param loadbalancer
     *         负载均衡器
     * @return 移动云发送处理
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Conditional(ChinaMobileSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    public ChinaMobileSendHandler huaWeiCloudSendHandler(ChinaMobileProperties properties, ObjectMapper objectMapper,
            SmsSenderLoadBalancer loadbalancer) {
        ChinaMobileSendHandler handler = new ChinaMobileSendHandler(properties, objectMapper);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    public static class ChinaMobileSendHandlerCondition implements Condition {

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.chinamobile.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
