package net.guerlab.sms.qiniu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 七牛云短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.qiniu")
public class QiNiuProperties extends AbstractHandlerProperties<String> {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

}
