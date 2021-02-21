package net.guerlab.sms.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置
 *
 * @author guer
 *
 */
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 手机号码正则规则
     */
    private String reg;

    /**
     * 负载均衡类型
     * <p>
     * 可选值:
     * Random、RoundRobin、WeightRandom、WeightRoundRobin，
     * 默认:
     * Random
     */
    private String loadBalancerType = "Random";
}
