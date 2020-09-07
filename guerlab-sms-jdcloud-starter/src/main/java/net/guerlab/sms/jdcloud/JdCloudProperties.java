package net.guerlab.sms.jdcloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * 京东云短信配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.jdcloud")
public class JdCloudProperties {

    /**
     * 短信模板
     */
    protected Map<String, String> templates;

    /**
     * 参数顺序
     */
    protected Map<String, List<String>> paramsOrders;

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String secretAccessKey;

    /**
     * 地域
     */
    private String region = "cn-north-1";

    /**
     * 签名ID
     */
    private String signId;

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

    /**
     * 返回参数顺序
     *
     * @param type
     *         类型
     * @return 参数顺序
     */
    public List<String> getParamsOrder(String type) {
        return paramsOrders.get(type);
    }

}
