package net.guerlab.sms.chinamobile;

import lombok.Data;

/**
 * 响应结果
 *
 * @author guer
 */
@Data
public class ChinaMobileResult {

    /**
     * 响应状态
     */
    public static final String SUCCESS_RSPCOD = "success";

    /**
     * 消息批次号，由云MAS平台生成，用于关联短信发送请求与状态报告，注：若数据验证不通过，该参数值为空
     */
    private String msgGroup;

    /**
     * 响应状态
     */
    private String rspcod;

    /**
     * 是否成功
     */
    private boolean success;
}
