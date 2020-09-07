package net.guerlab.sms.upyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 发送请求
 *
 * @author guer
 */
@Data
public class UpyunSendRequest {

    /**
     * 手机号
     */
    @JsonProperty("mobile")
    private String mobile;

    /**
     * 模板编号
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 短信参数
     */
    @JsonProperty("vars")
    private String vars;

    /**
     * 拓展码
     */
    @JsonProperty("extend_code")
    private String extendCode;
}
