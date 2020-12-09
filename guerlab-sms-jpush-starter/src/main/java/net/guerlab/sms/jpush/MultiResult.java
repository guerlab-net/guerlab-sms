package net.guerlab.sms.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 批量发送返回结果
 *
 * @author guer
 */
@Data
public class MultiResult {

    @JsonProperty("success_count")
    private Integer successCount;

    @JsonProperty("failure_count")
    private Integer failureCount;
}
