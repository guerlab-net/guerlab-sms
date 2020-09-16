package net.guerlab.sms.upyun;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 又拍云短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.upyun")
public class UpyunProperties extends AbstractHandlerProperties<String> {

    /**
     * token
     */
    private String token;

}
