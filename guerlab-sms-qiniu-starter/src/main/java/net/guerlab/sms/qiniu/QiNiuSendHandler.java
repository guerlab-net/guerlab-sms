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
package net.guerlab.sms.qiniu;

import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.server.handler.AbstractSendHandler;

import java.util.Collection;

/**
 * 七牛云发送处理
 *
 * @author guer
 */
@Slf4j
public class QiNiuSendHandler extends AbstractSendHandler<QiNiuProperties> {

    private final SmsManager smsManager;

    public QiNiuSendHandler(QiNiuProperties properties) {
        super(properties);
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        smsManager = new SmsManager(auth);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        String templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        try {
            Response response = smsManager
                    .sendMessage(templateId, phones.toArray(new String[] {}), noticeData.getParams());

            if (!response.isOK()) {
                log.debug("send fail, error: {}", response.error);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage(), e);
        }

        return false;
    }
}
