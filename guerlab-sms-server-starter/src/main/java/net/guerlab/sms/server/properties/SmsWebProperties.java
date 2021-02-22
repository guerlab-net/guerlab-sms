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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信Web配置
 *
 * @author guer
 *
 */
@Data
@ConfigurationProperties(prefix = "sms.web")
public class SmsWebProperties {

    /**
     * 默认基础路径
     */
    public static final String DEFAULT_BASE_PATH = "/sms";

    /**
     * 是否启用web端点
     */
    private boolean enable = false;

    /**
     * 基础路径
     */
    private String basePath = DEFAULT_BASE_PATH;

    /**
     * 是否启用验证码发送web端点
     */
    private boolean enableSend = true;

    /**
     * 是否启用验证码查询web端点
     */
    private boolean enableGet = true;

    /**
     * 是否启用验证码验证web端点
     */
    private boolean enableVerify = true;

    /**
     * 是否启用通知发送web端点
     */
    private boolean enableNotice = true;

}
