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
package net.guerlab.sms.core.exception;

import java.util.Locale;

/**
 * 未找到有效的短信发送处理
 *
 * @author guer
 *
 */
public class NotFindSendHandlerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "未找到有效的短信发送处理程序";
        } else {
            DEFAULT_MSG = "Not found effective sms send handler.";
        }
    }

    /**
     * 未找到有效的短信发送处理
     */
    public NotFindSendHandlerException() {
        super(DEFAULT_MSG);
    }
}
