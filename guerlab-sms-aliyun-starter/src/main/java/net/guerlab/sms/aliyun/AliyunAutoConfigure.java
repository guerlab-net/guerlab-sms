package net.guerlab.sms.aliyun;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Conditional(AliyunSendHandlerCondition.class)
    public AliyunSendHandler aliyunSendHandler(AliyunProperties properties, ObjectMapper objectMapper) {
        return new AliyunSendHandler(properties, objectMapper);
    }

    public static class AliyunSendHandlerCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Boolean enable = context.getEnvironment().getProperty("sms.aliyun.enable", Boolean.class);
            return enable == null ? true : enable;
        }
    }

}
