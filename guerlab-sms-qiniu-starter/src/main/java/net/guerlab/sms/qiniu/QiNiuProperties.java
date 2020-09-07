package net.guerlab.sms.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 七牛云短信配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.qiniu")
public class QiNiuProperties {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

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
