package net.guerlab.sms.qcloud;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import net.guerlab.sms.qcloud.QCloudAutoConfigure.EnableCondition;

/**
 * 阿里云发送端点自动配置
 *
 * @author guer
 *
 */
@Configuration
@Conditional(EnableCondition.class)
@EnableConfigurationProperties(QCloudProperties.class)
public class QCloudAutoConfigure {

    /**
     * 构造阿里云发送处理
     *
     * @param properties
     *            配置对象
     * @return 阿里云发送处理
     */
    @Bean
    @RefreshScope
    public QCloudSendHandler aliyunSendHandler(QCloudProperties properties) {
        return new QCloudSendHandler(properties);
    }

    /**
     * 启用判断
     *
     * @author guer
     *
     */
    public static class EnableCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return context.getEnvironment().getProperty("sms.qcloud.enable", Boolean.TYPE, true);
        }

    }

}
