package net.guerlab.sms.netease;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网易云信短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.netease")
public class NeteaseCloudProperties extends AbstractHandlerProperties<String> {

    /**
     * appkey
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;

}
