package net.guerlab.sms.huaweicloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * 华为云短信配置
 *
 * @author guer
 */
@Data
@ConfigurationProperties(prefix = "sms.huawei")
public class HuaWeiCloudProperties {

    /**
     * 短信模板
     */
    protected Map<String, String> templates;

    /**
     * 参数顺序
     */
    protected Map<String, List<String>> paramsOrders;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * APP_Key
     */
    private String appKey;

    /**
     * APP_Secret
     */
    private String appSecret;

    /**
     * 国内短信签名通道号或国际/港澳台短信通道号
     */
    private String sender;

    /**
     * 签名名称
     */
    private String signature;

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
