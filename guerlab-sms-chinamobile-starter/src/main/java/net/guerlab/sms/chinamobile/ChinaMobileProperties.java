package net.guerlab.sms.chinamobile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 移动云短信配置
 *
 * @author guer
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.chinamobile")
public class ChinaMobileProperties extends AbstractHandlerProperties<String> {

    /**
     * 请求地址
     */
    private String uri = "http://112.35.1.155:1992/sms/tmpsubmit";

    /**
     * 企业名称
     */
    private String ecName;

    /**
     * 接口账号用户名
     */
    private String apId;

    /**
     * 接口账号密码
     */
    private String secretKey;

    /**
     * 签名编码。在模板短信控制台概览页获取。
     */
    private String sign;

}
