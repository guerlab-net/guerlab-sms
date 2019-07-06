package net.guerlab.sms.aliyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云发送端点自动配置
 *
 * @author guer
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "sms.aliyun", name = "enable", havingValue = "true")
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

}
