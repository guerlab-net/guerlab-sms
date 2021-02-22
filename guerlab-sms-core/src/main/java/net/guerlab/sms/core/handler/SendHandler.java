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
package net.guerlab.sms.core.handler;

import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.utils.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 发送处理
 *
 * @author guer
 *
 */
public interface SendHandler {

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phones
     *            手机号列表
     * @return 是否发送成功
     */
    boolean send(NoticeData noticeData, Collection<String> phones);

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phone
     *            手机号列表
     * @return 是否发送成功
     */
    default boolean send(NoticeData noticeData, String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }

        return send(noticeData, Collections.singletonList(phone));
    }

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phones
     *            手机号列表
     * @return 是否发送成功
     */
    default boolean send(NoticeData noticeData, String... phones) {
        if (phones == null) {
            return false;
        }

        return send(noticeData, Arrays.asList(phones));
    }
}
