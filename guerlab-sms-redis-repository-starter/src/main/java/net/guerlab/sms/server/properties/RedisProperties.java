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
package net.guerlab.sms.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置
 *
 * @author guer
 */
@Component
@ConfigurationProperties(prefix = "sms.verification-code.repository.redis")
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
