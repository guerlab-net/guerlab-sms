package net.guerlab.sms.server.service;

import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.NotFindSendHandlerException;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.properties.SmsProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短信通知服务实现
 *
 * @author guer
 *
 */
@Slf4j
@Service
public class DefaultNoticeService implements NoticeService, ApplicationContextAware {

    private SmsProperties properties;

    private ApplicationContext context = null;

    @Autowired
    public void setProperties(SmsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public boolean phoneRegValidation(String phone) {
        return StringUtils.isNotBlank(phone)
                && (StringUtils.isBlank(properties.getReg()) || phone.matches(properties.getReg()));
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

        Map<String, SendHandler> sendHandlerMap = context.getBeansOfType(SendHandler.class);

        if (sendHandlerMap.isEmpty()) {
            throw new NotFindSendHandlerException();
        }

        return sendHandlerMap.values().stream().findAny().map(sendHandler -> sendHandler.send(noticeData, phones))
                .orElse(false);
    }
}
