package net.guerlab.sms.server.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 抽象处理实现配置
 *
 * @param <T>
 *         模板ID类型
 * @author guer
 */
public abstract class AbstractHandlerProperties<T> {

    /**
     * 权重
     */
    private int weight = 1;

    /**
     * 短信模板
     */
    @Setter
    @Getter
    private Map<String, T> templates;

    /**
     * 参数顺序
     */
    @Setter
    @Getter
    private Map<String, List<String>> paramsOrders;

    /**
     * 获取短信模板
     *
     * @param type
     *         类型
     * @return 短信模板
     */
    public final T getTemplates(String type) {
        return templates == null ? null : templates.get(type);
    }

    /**
     * 返回参数顺序
     *
     * @param type
     *         类型
     * @return 参数顺序
     */
    public final List<String> getParamsOrder(String type) {
        return paramsOrders.get(type);
    }

    /**
     * 获取权重,权重最小值为0
     *
     * @return 权重
     */
    public int getWeight() {
        return weight;
    }

    /**
     * 设置权重,权重最小值为0
     *
     * @param weight
     *         权重
     */
    public void setWeight(int weight) {
        if (weight >= 0) {
            this.weight = weight;
        }
    }
}
