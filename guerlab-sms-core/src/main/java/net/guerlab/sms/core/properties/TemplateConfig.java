package net.guerlab.sms.core.properties;

import java.util.Map;

/**
 * 基础短信配置类
 *
 * @author guer
 *
 */
public abstract class TemplateConfig {

    /**
     * 短信模板
     */
    protected Map<String, String> templates;

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
