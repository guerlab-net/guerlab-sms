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
package net.guerlab.sms.upyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 发送请求
 *
 * @author guer
 */
@Data
public class UpyunSendRequest {

    /**
     * 手机号
     */
    @JsonProperty("mobile")
    private String mobile;

    /**
     * 模板编号
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 短信参数
     */
    @JsonProperty("vars")
    private String vars;

    /**
     * 拓展码
     */
    @JsonProperty("extend_code")
    private String extendCode;
}
