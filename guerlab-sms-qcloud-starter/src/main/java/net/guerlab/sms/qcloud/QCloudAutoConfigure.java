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
package net.guerlab.sms.qcloud;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 腾讯云发送端点自动配置
 *
 * @author guer
 *
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Configuration
@EnableConfigurationProperties(QCloudProperties.class)
@AutoConfigureAfter(SmsConfiguration.class)
public class QCloudAutoConfigure {

    /**
     * 构造腾讯云发送处理
     *
     * @param properties
     *         配置对象
     * @param loadbalancer
     *         负载均衡器
     * @param eventPublisher
     *         spring应用事件发布器
     * @return 腾讯云发送处理
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Conditional(QCloudSendHandlerCondition.class)
    @ConditionalOnBean(SmsSenderLoadBalancer.class)
    public QCloudSendHandler qcloudSendHandler(QCloudProperties properties, SmsSenderLoadBalancer loadbalancer,
            ApplicationEventPublisher eventPublisher) {
        QCloudSendHandler handler = new QCloudSendHandler(properties, eventPublisher);
        loadbalancer.addTarget(handler, true);
        loadbalancer.setWeight(handler, properties.getWeight());
        return handler;
    }

    @SuppressWarnings("AlibabaClassNamingShouldBeCamel")
    public static class QCloudSendHandlerCondition implements Condition {

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.qcloud.enable", Boolean.class);
            return enable == null || enable;
        }
    }

}
