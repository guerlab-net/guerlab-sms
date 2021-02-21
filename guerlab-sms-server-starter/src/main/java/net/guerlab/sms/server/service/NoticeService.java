package net.guerlab.sms.server.service;

import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.utils.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
     * @return 是否发送成功
     */
    boolean send(NoticeData noticeData, Collection<String> phones);

    /**
     * 发送通知
     *
     * @param noticeData
     *            通知内容
     * @param phone
     *            手机号
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
        if (phones == null || phones.length <= 0) {
            return false;
        }

        return send(noticeData, Arrays.asList(phones));
    }

    /**
     * 异步发送通知,当未启用异步支持的时候默认会直接同步发送
     *
     * @param noticeData
     *         通知内容
     * @param phones
     *         手机号列表
     */
    void asyncSend(NoticeData noticeData, Collection<String> phones);

    /**
     * 异步发送通知,当未启用异步支持的时候默认会直接同步发送
     *
     * @param noticeData
     *         通知内容
     * @param phone
     *         手机号
     */
    default void asyncSend(NoticeData noticeData, String phone) {
        if (!StringUtils.isBlank(phone)) {
            asyncSend(noticeData, Collections.singletonList(phone));
        }
    }

    /**
     * 异步发送通知,当未启用异步支持的时候默认会直接同步发送
     *
     * @param noticeData
     *         通知内容
     * @param phones
     *         手机号列表
     */
    default void asyncSend(NoticeData noticeData, String... phones) {
        if (phones != null && phones.length > 0) {
            asyncSend(noticeData, Arrays.asList(phones));
        }
    }
}
