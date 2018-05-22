package net.guerlab.sms.core.handler;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import net.guerlab.sms.core.domain.NoticeData;

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
     */
    void send(NoticeData noticeData, Collection<String> phones);

    /**
     * 发送通知
     * 
     * @param noticeData
     *            通知内容
     * @param phone
     *            手机号列表
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
