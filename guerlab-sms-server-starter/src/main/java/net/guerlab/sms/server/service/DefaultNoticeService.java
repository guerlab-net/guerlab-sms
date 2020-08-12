package net.guerlab.sms.server.service;

import lombok.extern.slf4j.Slf4j;
import net.guerlab.loadbalancer.ILoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.NotFindSendHandlerException;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.properties.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信通知服务实现
 *
 * @author guer
 */
@Slf4j
@Service
public class DefaultNoticeService implements NoticeService {

    private SmsProperties properties;

    private ILoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer;

    @Override
    public boolean phoneRegValidation(String phone) {
        return StringUtils.isNotBlank(phone) && (StringUtils.isBlank(properties.getReg()) || phone
                .matches(properties.getReg()));
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        if (noticeData == null) {
            log.debug("noticeData is null");
            return false;
        }

        if (phones == null || phones.isEmpty()) {
            log.debug("phones is empty");
            return false;
        }

        List<String> phoneList = phones.stream().filter(this::phoneRegValidation).collect(Collectors.toList());

        if (phoneList.isEmpty()) {
            log.debug("after filter phones is empty");
            return false;
        }

        SendHandler sendHandler = smsSenderLoadbalancer.choose(noticeData);

        if (sendHandler == null) {
            throw new NotFindSendHandlerException();
        }

        return sendHandler.send(noticeData, phones);
    }

    @Autowired
    public void setProperties(SmsProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setSmsSenderLoadbalancer(ILoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer) {
        this.smsSenderLoadbalancer = smsSenderLoadbalancer;
    }
}
