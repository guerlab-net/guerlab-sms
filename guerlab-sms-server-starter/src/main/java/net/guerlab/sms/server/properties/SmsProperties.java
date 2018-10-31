package net.guerlab.sms.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 *
 * @author guer
 *
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 手机号码正则规则
     */
    private String reg;

    /**
     * 验证码配置
     */
    private VerificationCodeProperties verificationCode = new VerificationCodeProperties();

    /**
     * 短信Web配置
     */
    private SmsWebProperties web = new SmsWebProperties();

    /**
     * 返回 手机号码正则规则
     *
     * @return 手机号码正则规则
     */
    public String getReg() {
        return reg;
    }

    /**
     * 设置手机号码正则规则
     *
     * @param reg
     *            手机号码正则规则
     */
    public void setReg(String reg) {
        this.reg = reg;
    }

    /**
     * 返回验证码配置
     *
     * @return 验证码配置
     */
    public VerificationCodeProperties getVerificationCode() {
        return verificationCode;
    }

    /**
     * 设置验证码配置
     *
     * @param verificationCode
     *            验证码配置
     */
    public void setVerificationCode(VerificationCodeProperties verificationCode) {
        this.verificationCode = verificationCode;
    }

    /**
     * 返回短信Web配置
     *
     * @return 短信Web配置
     */
    public SmsWebProperties getWeb() {
        return web;
    }

    /**
     * 设置短信Web配置
     *
     * @param web
     *            短信Web配置
     */
    public void setWeb(SmsWebProperties web) {
        this.web = web;
    }
}
