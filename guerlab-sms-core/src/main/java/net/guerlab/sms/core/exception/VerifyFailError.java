package net.guerlab.sms.core.exception;

import java.util.Locale;

import net.guerlab.commons.exception.ApplicationException;

/**
 * 验证失败
 *
 * @author guer
 *
 */
public class VerifyFailError extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "验证失败";
        } else {
            DEFAULT_MSG = "Validation fails";
        }
    }

    /**
     * 验证失败
     */
    public VerifyFailError() {
        super(DEFAULT_MSG);
    }
}
