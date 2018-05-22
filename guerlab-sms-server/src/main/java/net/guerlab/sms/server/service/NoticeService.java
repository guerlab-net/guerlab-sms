package net.guerlab.sms.server.service;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import net.guerlab.sms.core.domain.NoticeData;

/**
 * 短信通知服务
 *
 * @author guer
 *
 */
public interface NoticeService {

    /**
     * 手机号码规则验证
     *
     * @param phone
     *            手机号码
     * @return 是否验证通过
     */
    boolean phoneRegValidation(String phone);

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phones
     *            手机号列表
     */
    void send(NoticeData noticeData, Collection<String> phones);

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phone
     *            手机号
     */
    default void send(NoticeData noticeData, String phone) {
        if (StringUtils.isBlank(phone)) {
            return;
        }

        send(noticeData, Arrays.asList(phone));
    }

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phones
     *            手机号列表
     */
    default void send(NoticeData noticeData, String... phones) {
        if (phones == null) {
            return;
        }

        send(noticeData, Arrays.asList(phones));
    }
}
