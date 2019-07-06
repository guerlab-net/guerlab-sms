package net.guerlab.sms.server.service;

/**
 * 手机验证码服务
 *
 * @author guer
 *
 */
public interface VerificationCodeService {

    /**
     * 验证码短信中验证码对应的key
     */
    String MSG_KEY_CODE = "code";

    /**
     * 验证码短信中识别码对应的key
     */
    String MSG_KEY_IDENTIFICATION_CODE = "identificationCode";

    /**
     * 验证码短信中有效期(秒)对应的key
     */
    String MSG_KEY_EXPIRATION_TIME_OF_SECONDS = "expirationTimeOfSeconds";

    /**
     * 验证码短信中有效期(分)对应的key
     */
    String MSG_KEY_EXPIRATION_TIME_OF_MINUTES = "expirationTimeOfMinutes";

    /**
     * 查询手机验证码
     *
     * @param phone
     *            手机号
     * @param identificationCode
     *            识别码
     * @return 手机验证码
     */
    String find(String phone, String identificationCode);

    /**
     * 发送验证码
     *
     * @param phone
     *            手机号码
     */
    void send(String phone);

    /**
     * 验证
     *
     * @param phone
     *            手机号码
     * @param code
     *            验证码
     * @param identificationCode
     *            识别码
     * @return 验证通过
     */
    boolean verify(String phone, String code, String identificationCode);
}
