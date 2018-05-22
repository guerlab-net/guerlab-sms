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
     * 启用web端点
     */
    private boolean enableWeb = false;

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
     * 返回启用web端点
     *
     * @return 启用web端点
     */
    public boolean isEnableWeb() {
        return enableWeb;
    }

    /**
     * 设置启用web端点
     *
     * @param enableWeb
     *            启用web端点
     */
    public void setEnableWeb(boolean enableWeb) {
        this.enableWeb = enableWeb;
    }
}
