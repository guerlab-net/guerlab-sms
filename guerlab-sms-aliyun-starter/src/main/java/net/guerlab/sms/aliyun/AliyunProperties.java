package net.guerlab.sms.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 阿里云短信配置
 *
 * @author guer
 *
 */
@Data
@ConfigurationProperties(prefix = "sms.aliyun")
public class AliyunProperties {

    /**
     * Endpoint
     */
    private String endpoint = "cn-hangzhou";

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板
     */
    protected Map<String, String> templates;

    /**
     * 获取短信模板
     *
     * @param type
     *            类型
     * @return 短信模板
     */
    public String getTemplates(String type) {
        return templates == null ? null : templates.get(type);
    }

}
