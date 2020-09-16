package net.guerlab.sms.baiducloud;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度云短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.baiducloud")
public class BaiduCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * ACCESS_KEY_ID
     */
    private String accessKeyId;

    /**
     * SECRET_ACCESS_KEY
     */
    private String secretAccessKey;

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * 短信签名ID
     */
    private String signatureId;
}
