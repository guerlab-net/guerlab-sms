package net.guerlab.sms.server.properties;

/**
 * 短信Web配置
 *
 * @author guer
 *
 */
public class SmsWebProperties {

    /**
     * 默认基础路径
     */
    public static final String DEFAULT_BASE_PATH = "/sms";

    /**
     * 是否启用web端点
     */
    private boolean enable = false;

    /**
     * 基础路径
     */
    private String basePath = DEFAULT_BASE_PATH;

    /**
     * 返回是否启用web端点
     *
     * @return 是否启用web端点
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * 设置是否启用web端点
     *
     * @param enable
     *            是否启用web端点
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 返回基础路径
     *
     * @return 基础路径
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * 设置基础路径
     *
     * @param basePath
     *            基础路径
     */
    public void setBasePath(String basePath) {
        if (basePath == null) {
            return;
        }

        this.basePath = basePath;
    }

}
