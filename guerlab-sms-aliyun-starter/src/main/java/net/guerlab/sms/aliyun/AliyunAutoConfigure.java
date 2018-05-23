package net.guerlab.sms.aliyun;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.guerlab.sms.aliyun.AliyunAutoConfigure.EnableCondition;

/**
 * 阿里云发送端点自动配置
 *
 * @author guer
 *
 */
@Configuration
@Conditional(EnableCondition.class)
@EnableConfigurationProperties(AliyunProperties.class)
public class AliyunAutoConfigure {

    /**
     * 构造阿里云发送处理
     *
     * @param properties
     *            配置对象
     * @param objectMapper
     *            objectMapper
     * @return 阿里云发送处理
     */
    @Bean
    @RefreshScope
    public AliyunSendHandler aliyunSendHandler(AliyunProperties properties, ObjectMapper objectMapper) {
        return new AliyunSendHandler(properties, objectMapper);
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
            return context.getEnvironment().getProperty("sms.aliyun.enable", Boolean.TYPE, true);
        }

    }

}
