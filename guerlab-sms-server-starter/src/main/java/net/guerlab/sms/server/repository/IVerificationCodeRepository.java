package net.guerlab.sms.server.repository;

import net.guerlab.sms.server.entity.VerificationCode;

/**
 * 验证码储存接口
 * 
 * @author guer
 *
 */
public interface IVerificationCodeRepository {

    /**
     * 根据手机号码查询验证码
     *
     * @param phone
     *            手机号码
     * @param identificationCode
     *            识别码
     * @return 验证码
     */
    VerificationCode findOne(String phone, String identificationCode);

    /**
     * 保存验证码
     *
     * @param verificationCode
     *            验证码
     */
    void save(VerificationCode verificationCode);

    /**
     * 删除验证码
     *
     * @param phone
     *            手机号码
     * @param identificationCode
     *            识别码
     */
    void delete(String phone, String identificationCode);

}
