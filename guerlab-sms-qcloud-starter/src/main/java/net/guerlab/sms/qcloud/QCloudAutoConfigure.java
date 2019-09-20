package net.guerlab.sms.qcloud;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 腾讯云发送端点自动配置
 *
 * @author guer
 *
 */
@Configuration
@EnableConfigurationProperties(QCloudProperties.class)
public class QCloudAutoConfigure {

    /**
     * 构造腾讯云发送处理
     *
     * @param properties
     *            配置对象
     * @return 腾讯云发送处理
     */
    @Bean
    @Conditional(QCloudSendHandlerCondition.class)
    public QCloudSendHandler qcloudSendHandler(QCloudProperties properties) {
        return new QCloudSendHandler(properties);
    }

    public static class QCloudSendHandlerCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.qcloud.enable", Boolean.class);
            return enable == null ? true : enable;
        }
    }

}
