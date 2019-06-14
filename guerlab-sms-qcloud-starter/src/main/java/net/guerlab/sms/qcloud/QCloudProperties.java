package net.guerlab.sms.qcloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;
import java.util.Map;

/**
 * 腾讯云短信配置
 *
 * @author guer
 *
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "sms.qcloud")
public class QCloudProperties {

    /**
     * 短信应用SDK AppID
     */
    private int appId;

    /**
     * 短信应用SDK AppKey
     */
    private String appkey;

    /**
     * 短信签名
     */
    private String smsSign;

    /**
     * 短信模板
     */
    protected Map<String, Integer> templates;

    /**
     * 参数顺序
     */
    protected Map<String, List<String>> paramsOrders;

    /**
     * 获取短信模板
     *
     * @param type
     *            类型
     * @return 短信模板
     */
    public Integer getTemplates(String type) {
        return templates == null ? null : templates.get(type);
    }

    /**
     * 返回参数顺序
     *
     * @param type
     *            类型
     * @return 参数顺序
     */
    public List<String> getParamsOrder(String type) {
        return paramsOrders.get(type);
    }

}
