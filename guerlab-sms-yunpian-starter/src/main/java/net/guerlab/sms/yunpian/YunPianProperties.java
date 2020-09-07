package net.guerlab.sms.yunpian;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 云片云短信配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.yunpian")
public class YunPianProperties {

    /**
     * apikey
     */
    private String apikey;

    /**
     * 短信模板
     */
    private Map<String, String> templates;

    /**
     * 获取短信模板
     *
     * @param type
     *         类型
     * @return 短信模板
     */
    public String getTemplates(String type) {
        return templates == null ? null : templates.get(type);
    }

}
