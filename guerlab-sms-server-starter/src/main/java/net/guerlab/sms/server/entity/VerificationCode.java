package net.guerlab.sms.server.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 验证码
 *
 * @author guer
 */
@Data
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
}
