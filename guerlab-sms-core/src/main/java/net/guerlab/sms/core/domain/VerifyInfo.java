package net.guerlab.sms.core.domain;

import lombok.Data;

/**
 * 验证信息
 * 
 * @author guer
 *
 */
@Data
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
}
