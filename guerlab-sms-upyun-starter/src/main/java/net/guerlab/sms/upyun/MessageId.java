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
import net.guerlab.sms.core.utils.StringUtils;

/**
 * 手机号发送短信的结果
 *
 * @author guer
 */
@Data
public class MessageId {

    /**
     * 错误情况
     */
    @JsonProperty("error_code")
    private String errorCode;

    /**
     * 旧版本国内短信的 message 编号
     */
    @JsonProperty("message_id")
    private Integer messageId;

    /**
     * message 编号
     */
    @JsonProperty("msg_id")
    private String msgId;

    /**
     * 手机号
     */
    @JsonProperty("mobile")
    private String mobile;

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean succeed() {
        return StringUtils.isBlank(errorCode) && StringUtils.isNotBlank(msgId);
    }
}
