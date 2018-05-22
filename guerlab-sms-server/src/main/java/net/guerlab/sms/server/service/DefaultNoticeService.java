package net.guerlab.sms.server.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guerlab.commons.collection.CollectionUtil;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.NotFindSendHandlerError;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.server.properties.SmsProperties;
import net.guerlab.spring.commons.util.SpringApplicationContextUtil;

/**
 * 短信通知服务实现
 *
 * @author guer
 *
 */
@Service
public class DefaultNoticeService implements NoticeService {

    @Autowired
    private SmsProperties properties;

    @Override
    public boolean phoneRegValidation(String phone) {
        return StringUtils.isNotBlank(phone)
                && (StringUtils.isBlank(properties.getReg()) || phone.matches(properties.getReg()));
    }

    @Override
    public void send(NoticeData noticeData, Collection<String> phones) {
        if (noticeData == null || CollectionUtil.isEmpty(phones)) {
            return;
        }

        List<String> phoneList = phones.stream().filter(this::phoneRegValidation).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(phoneList)) {
            return;
        }

        Map<String, SendHandler> sendHandlerMap = SpringApplicationContextUtil.getContext()
                .getBeansOfType(SendHandler.class);

        if (sendHandlerMap.isEmpty()) {
            throw new NotFindSendHandlerError();
        }

        Optional<SendHandler> optional = sendHandlerMap.values().stream().findAny();

        optional.ifPresent(handler -> handler.send(noticeData, phones));
    }

}
