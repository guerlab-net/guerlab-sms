package net.guerlab.sms.yunpian;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 云片网短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.yunpian")
public class YunPianProperties extends AbstractHandlerProperties<String> {

    /**
     * apikey
     */
    private String apikey;

}
