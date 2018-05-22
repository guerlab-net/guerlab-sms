package net.guerlab.sms.core.domain;

import java.util.Collections;
import java.util.Map;

/**
 * 通知内容
 *
 * @author guer
 *
 */
public class NoticeData {

    /**
     * 类型
     */
    private String type;

    /**
     * 参数列表
     */
    private Map<String, String> params;

    /**
     * 返回 类型
     *
     * @return 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type
     *            类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 返回 参数列表
     *
     * @return 参数列表
     */
    public Map<String, String> getParams() {
        return params == null ? Collections.emptyMap() : params;
    }

    /**
     * 设置参数列表
     *
     * @param params
     *            参数列表
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
