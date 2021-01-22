package net.guerlab.sms.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码内存储存配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.verification-code.repository.memory")
public class VerificationCodeMemoryRepositoryProperties {

    /**
     * 默认gc频率，单位秒
     */
    public static final long DEFAULT_GC_FREQUENCY = 300L;

    /**
     * gc频率，单位秒
     */
    private long gcFrequency = DEFAULT_GC_FREQUENCY;
}
