package net.guerlab.sms.core.exception;

import java.util.Locale;

/**
 * 短信发送客户端错误
 *
 * @author guer
 */
public class SendClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "短信发送失败，客户端错误，";
        } else {
            DEFAULT_MSG = "SMS sending failed with client exception, ";
        }
    }

    /**
     * 通过错误信息构造短信发送客户端错误
     *
     * @param message
     *         错误信息
     */
    public SendClientException(String message) {
        super(DEFAULT_MSG + message);
    }
}
