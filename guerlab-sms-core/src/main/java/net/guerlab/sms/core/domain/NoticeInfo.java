package net.guerlab.sms.core.domain;

import java.util.Collection;

/**
 * 通知信息
 * 
 * @author guer
 *
 */
public class NoticeInfo {

    /**
     * 通知内容
     */
    private NoticeData noticeData;

    /**
     * 号码列表
     */
    private Collection<String> phones;

    /**
     * 返回 通知内容
     *
     * @return 通知内容
     */
    public NoticeData getNoticeData() {
        return noticeData;
    }

    /**
     * 设置通知内容
     *
     * @param noticeData
     *            通知内容
     */
    public void setNoticeData(NoticeData noticeData) {
        this.noticeData = noticeData;
    }

    /**
     * 返回 号码列表
     *
     * @return 号码列表
     */
    public Collection<String> getPhones() {
        return phones;
    }

    /**
     * 设置号码列表
     *
     * @param phones
     *            号码列表
     */
    public void setPhones(Collection<String> phones) {
        this.phones = phones;
    }
}
