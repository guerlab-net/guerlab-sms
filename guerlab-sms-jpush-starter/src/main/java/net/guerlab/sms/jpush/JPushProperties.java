package net.guerlab.sms.jpush;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 极光短信配置
 *
 * @author guer
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.jpush")
public class JPushProperties extends AbstractHandlerProperties<Integer> {

    /**
     * appKey
     */
    private String appKey;

    /**
     * masterSecret
     */
    private String masterSecret;

    /**
     * 签名ID
     */
    private Integer signId;

}
