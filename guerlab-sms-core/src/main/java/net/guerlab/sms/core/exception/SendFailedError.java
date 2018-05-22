package net.guerlab.sms.core.exception;

import java.util.Locale;

import net.guerlab.commons.exception.ApplicationException;

/**
 * 短信发送失败
 *
 * @author guer
 *
 */
public class SendFailedError extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "短信发送失败，";
        } else {
            DEFAULT_MSG = "SMS sending failed,";
        }
    }

    /**
     * 通过错误信息构造短信发送失败异常
     *
     * @param message
     *            错误信息
     */
    public SendFailedError(String message) {
        super(DEFAULT_MSG + message);
    }
}
