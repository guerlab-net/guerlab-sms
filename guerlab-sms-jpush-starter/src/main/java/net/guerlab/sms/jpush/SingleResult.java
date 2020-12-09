package net.guerlab.sms.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 单条发送返回结果
 *
 * @author guer
 */
@Data
public class SingleResult {

    @JsonProperty("msg_id")
    private Integer msgId;

    private ErrorInfo error;

    @Data
    public static final class ErrorInfo {

        private String code;

        private String message;
    }
}
