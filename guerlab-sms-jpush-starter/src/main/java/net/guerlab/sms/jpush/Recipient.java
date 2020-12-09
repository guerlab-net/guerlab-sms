package net.guerlab.sms.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 接收者
 *
 * @author guer
 */
@Data
public class Recipient {

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 签名ID
     */
    @JsonProperty("sign_id")
    private Integer signId;

    /**
     * 模板ID
     */
    @JsonProperty("temp_id")
    private Integer tempId;

    /**
     * 模板参数
     */
    @JsonProperty("temp_para")
    private Map<String, String> tempPara;
}
