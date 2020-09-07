package net.guerlab.sms.upyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * 又拍云短信配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.upyun")
public class UpyunProperties {

    /**
     * 参数顺序
     */
    protected Map<String, List<String>> paramsOrders;

    /**
     * token
     */
    private String token;

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
