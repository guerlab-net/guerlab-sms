package net.guerlab.sms.server.entity;

import java.time.LocalDateTime;

/**
 * 验证码
 *
 * @author guer
 */
public class VerificationCode {

    /**
     * 类型
     */
    public static final String TYPE = "VerificationCode";

    /**
     * 手机号
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
     * 可重试时间
     */
    private LocalDateTime retryTime;

    /**
     * 过期时间
     */
    private LocalDateTime expirationTime;

    /**
     * 返回 手机号
     *
     * @return 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone
     *            手机号
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

    /**
     * 返回 可重试时间
     *
     * @return 可重试时间
     */
    public LocalDateTime getRetryTime() {
        return retryTime;
    }

    /**
     * 设置可重试时间
     *
     * @param retryTime 可重试时间
     */
    public void setRetryTime(LocalDateTime retryTime) {
        this.retryTime = retryTime;
    }

    /**
     * 返回 过期时间
     *
     * @return 过期时间
     */
    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    /**
     * 设置过期时间
     *
     * @param expirationTime
     *            过期时间
     */
    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
