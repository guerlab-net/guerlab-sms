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
package net.guerlab.sms.huaweicloud;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 华为云短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.huawei")
public class HuaWeiCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * 请求地址
     */
    private String uri;

    /**
     * APP_Key
     */
    private String appKey;

    /**
     * APP_Secret
     */
    private String appSecret;

    /**
     * 国内短信签名通道号或国际/港澳台短信通道号
     */
    private String sender;

    /**
     * 签名名称
     */
    private String signature;

}
