package net.guerlab.sms.aliyun;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 阿里云短信配置
 *
 * @author guer
 *
 */
@RefreshScope
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
     * 返回Endpoint
     *
     * @return Endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * 设置Endpoint
     *
     * @param endpoint
     *            Endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 返回 accessKeyId
     *
     * @return accessKeyId
     */
    public String getAccessKeyId() {
        return accessKeyId;
    }

    /**
     * 设置accessKeyId
     *
     * @param accessKeyId
     *            accessKeyId
     */
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    /**
     * 返回 accessKeySecret
     *
     * @return accessKeySecret
     */
    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    /**
     * 设置accessKeySecret
     *
     * @param accessKeySecret
     *            accessKeySecret
     */
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * 返回 短信签名
     *
     * @return 短信签名
     */
    public String getSignName() {
        return signName;
    }

    /**
     * 设置短信签名
     *
     * @param signName
     *            短信签名
     */
    public void setSignName(String signName) {
        this.signName = signName;
    }

    /**
     * 返回 短信模板
     *
     * @return 短信模板
     */
    public Map<String, String> getTemplates() {
        return templates;
    }

    /**
     * 设置短信模板
     *
     * @param templates
     *            短信模板
     */
    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

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
