package net.guerlab.sms.server.service;

/**
 * 手机验证码服务
 *
 * @author guer
 *
 */
public interface VerificationCodeService {

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
