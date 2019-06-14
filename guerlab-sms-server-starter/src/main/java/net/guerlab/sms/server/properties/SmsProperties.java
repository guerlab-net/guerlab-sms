package net.guerlab.sms.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 短信配置
 *
 * @author guer
 *
 */
@Data
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
}
