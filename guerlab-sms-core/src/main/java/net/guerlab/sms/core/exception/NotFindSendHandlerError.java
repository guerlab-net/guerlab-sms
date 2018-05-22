package net.guerlab.sms.core.exception;

import java.util.Locale;

import net.guerlab.commons.exception.ApplicationException;

/**
 * 未找到有效的短信发送处理
 *
 * @author guer
 *
 */
public class NotFindSendHandlerError extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "未找到有效的短信发送处理程序";
        } else {
            DEFAULT_MSG = "Not found effective sms send handler.";
        }
    }

    /**
     * 未找到有效的短信发送处理
     */
    public NotFindSendHandlerError() {
        super(DEFAULT_MSG);
    }
}
