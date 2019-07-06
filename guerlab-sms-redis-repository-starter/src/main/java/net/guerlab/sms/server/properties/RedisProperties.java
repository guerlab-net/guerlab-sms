package net.guerlab.sms.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * redis配置
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "sms.redis")
public class RedisProperties {

    /**
     * Key前缀
     */
    private String keyPrefix;

    /**
     * 获取Key前缀
     *
     * @return Key前缀
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * 设置Key前缀
     *
     * @param keyPrefix Key前缀
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}