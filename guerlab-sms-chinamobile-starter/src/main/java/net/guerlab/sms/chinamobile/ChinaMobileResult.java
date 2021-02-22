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
package net.guerlab.sms.chinamobile;

import lombok.Data;

/**
 * 响应结果
 *
 * @author guer
 */
@Data
public class ChinaMobileResult {

    /**
     * 响应状态
     */
    public static final String SUCCESS_RSPCOD = "success";

    /**
     * 消息批次号，由云MAS平台生成，用于关联短信发送请求与状态报告，注：若数据验证不通过，该参数值为空
     */
    private String msgGroup;

    /**
     * 响应状态
     */
    private String rspcod;

    /**
     * 是否成功
     */
    private boolean success;
}
