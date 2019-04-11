package net.guerlab.sms.core.exception;

import net.guerlab.commons.exception.ApplicationException;

import java.util.Locale;

/**
 * 重试时间过短
 *
 * @author guer
 */
public class RetryTimeShortException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    /**
     * 剩余时间,单位秒
     */
    private long surplus;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "重试时间过短，请等待%d秒后重试";
        } else {
            DEFAULT_MSG = "Retry time is short, please wait %d second and try again.";
        }
    }

    /**
     * 重试时间过短
     *
     * @param surplus 剩余时间,单位秒
     */
    public RetryTimeShortException(long surplus) {
        super(String.format(DEFAULT_MSG, surplus));
        this.surplus = surplus;
    }

    /**
     * 获取剩余时间,单位秒
     *
     * @return 剩余时间
     */
    public long getSurplus() {
        return surplus;
    }
}
