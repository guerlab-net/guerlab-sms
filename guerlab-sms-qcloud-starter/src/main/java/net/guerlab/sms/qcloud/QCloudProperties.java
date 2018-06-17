package net.guerlab.sms.qcloud;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 腾讯云短信配置
 *
 * @author guer
 *
 */
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
     * 返回短信应用SDK AppID
     *
     * @return 短信应用SDK AppID
     */
    public int getAppId() {
        return appId;
    }

    /**
     * 设置短信应用SDK AppID
     *
     * @param appId
     *            短信应用SDK AppID
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * 返回短信应用SDK AppKey
     *
     * @return 短信应用SDK AppKey
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置短信应用SDK AppKey
     *
     * @param appkey
     *            短信应用SDK AppKey
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    /**
     * 返回短信签名
     *
     * @return 短信签名
     */
    public String getSmsSign() {
        return smsSign;
    }

    /**
     * 设置短信签名
     *
     * @param smsSign
     *            短信签名
     */
    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign;
    }

    /**
     * 返回 短信模板
     *
     * @return 短信模板
     */
    public Map<String, Integer> getTemplates() {
        return templates;
    }

    /**
     * 设置短信模板
     *
     * @param templates
     *            短信模板
     */
    public void setTemplates(Map<String, Integer> templates) {
        this.templates = templates;
    }

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

}
