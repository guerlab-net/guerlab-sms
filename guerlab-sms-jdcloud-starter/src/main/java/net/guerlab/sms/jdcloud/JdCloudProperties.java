package net.guerlab.sms.jdcloud;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 京东云短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.jdcloud")
public class JdCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String secretAccessKey;

    /**
     * 地域
     */
    private String region = "cn-north-1";

    /**
     * 签名ID
     */
    private String signId;

}
