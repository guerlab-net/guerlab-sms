package net.guerlab.sms.baiducloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 百度云短信配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.baiducloud")
public class BaiduCloudProperties {

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
