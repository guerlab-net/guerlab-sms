package net.guerlab.sms.core.domain;

/**
 * 验证信息
 * 
 * @author guer
 *
 */
public class VerifyInfo {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 识别码
     */
    private String identificationCode;

    /**
     * 返回 手机号码
     *
     * @return 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号码
     *
     * @param phone
     *            手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 返回 验证码
     *
     * @return 验证码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置验证码
     *
     * @param code
     *            验证码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 返回识别码
     *
     * @return 识别码
     */
    public String getIdentificationCode() {
        return identificationCode;
    }

    /**
     * 设置识别码
     *
     * @param identificationCode
     *            识别码
     */
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
}
