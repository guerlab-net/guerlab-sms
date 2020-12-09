package net.guerlab.sms.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

/**
 * 接收者列表
 *
 * @author guer
 */
@Data
public class MultiRecipient {

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
     * 标签
     */
    private String tag;

    /**
     * 接收者列表
     */
    private Collection<Recipient> recipients;
}
