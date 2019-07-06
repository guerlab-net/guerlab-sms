package net.guerlab.sms.qcloud;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云发送端点自动配置
 *
 * @author guer
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "sms.qcloud", name = "enable", havingValue = "true")
@EnableConfigurationProperties(QCloudProperties.class)
public class QCloudAutoConfigure {

    /**
     * 构造腾讯云发送处理
     *
     * @param properties
     *            配置对象
     * @return 腾讯云发送处理Ø
     */
    @Bean
    @RefreshScope
    public QCloudSendHandler qcloudSendHandler(QCloudProperties properties) {
        return new QCloudSendHandler(properties);
    }

}
