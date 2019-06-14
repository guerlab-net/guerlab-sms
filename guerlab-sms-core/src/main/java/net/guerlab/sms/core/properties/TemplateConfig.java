package net.guerlab.sms.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 基础短信配置类
 *
 * @author guer
 *
 */
@Setter
@Getter
public abstract class TemplateConfig {

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
