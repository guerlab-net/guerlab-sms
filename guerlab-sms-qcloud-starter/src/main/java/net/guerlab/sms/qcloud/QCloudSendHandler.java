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
package net.guerlab.sms.qcloud;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.handler.AbstractSendHandler;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

/**
 * 腾讯云发送处理
 *
 * @author guer
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Slf4j
public class QCloudSendHandler extends AbstractSendHandler<QCloudProperties> {

    private static final String DEFAULT_NATION_CODE = "86";

    private final SmsMultiSender sender;

    public QCloudSendHandler(QCloudProperties properties, ApplicationEventPublisher eventPublisher) {
        super(properties, eventPublisher);
        sender = new SmsMultiSender(properties.getAppId(), properties.getAppkey());
    }

    @Override
    public String getChannelName() {
        return "qCloud";
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        Integer templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        List<String> paramsOrder = properties.getParamsOrder(type);

        ArrayList<String> params = new ArrayList<>();

        if (!paramsOrder.isEmpty()) {
            Map<String, String> paramMap = noticeData.getParams();
            for (String paramName : paramsOrder) {
                String paramValue = paramMap.get(paramName);

                params.add(paramValue);
            }
        }

        Map<String, ArrayList<String>> phoneMap = new HashMap<>(phones.size());

        for (String phone : phones) {
            if (StringUtils.isBlank(phone)) {
                continue;
            }
            if (phone.startsWith("+")) {
                String[] values = phone.split(" ");

                if (values.length == 1) {
                    getList(phoneMap, DEFAULT_NATION_CODE).add(phone);
                } else {
                    String nationCode = values[0];
                    String phoneNumber = StringUtils.join(values, "", 1, values.length);

                    getList(phoneMap, nationCode).add(phoneNumber);
                }

            } else {
                getList(phoneMap, DEFAULT_NATION_CODE).add(phone);
            }
        }

        boolean result = phoneMap.entrySet().parallelStream()
                .allMatch(entry -> send0(templateId, params, entry.getKey(), entry.getValue()));

        if (result) {
            publishSendEndEvent(noticeData, phones);
        }

        return result;
    }

    private Collection<String> getList(Map<String, ArrayList<String>> phoneMap, String nationCode) {
        return phoneMap.computeIfAbsent(nationCode, k -> new ArrayList<>());
    }

    private boolean send0(int templateId, ArrayList<String> params, String nationCode, ArrayList<String> phones) {
        try {
            SmsMultiSenderResult result = sender
                    .sendWithParam(nationCode, phones, templateId, params, properties.getSmsSign(), "", "");

            if (result.result == 0) {
                return true;
            }

            log.debug("send fail[code={}, errMsg={}]", result.result, result.errMsg);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        return false;
    }
}
