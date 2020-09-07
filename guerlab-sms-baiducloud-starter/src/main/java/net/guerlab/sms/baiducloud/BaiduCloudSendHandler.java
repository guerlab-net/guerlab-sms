package net.guerlab.sms.baiducloud;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.SmsClientConfiguration;
import com.baidubce.services.sms.model.SendMessageV3Request;
import com.baidubce.services.sms.model.SendMessageV3Response;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.core.utils.StringUtils;

import java.util.Collection;

/**
 * 百度云发送处理
 *
 * @author guer
 */
@Slf4j
public class BaiduCloudSendHandler implements SendHandler {

    private final BaiduCloudProperties properties;

    private final SmsClient client;

    public BaiduCloudSendHandler(BaiduCloudProperties properties) {
        this.properties = properties;

        SmsClientConfiguration config = new SmsClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(properties.getAccessKeyId(), properties.getSecretAccessKey()));
        config.setEndpoint(properties.getEndpoint());
        client = new SmsClient(config);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        String templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        SendMessageV3Request request = new SendMessageV3Request();
        request.setMobile(StringUtils.join(phones, ","));
        request.setSignatureId("");
        request.setTemplate(templateId);
        request.setContentVar(noticeData.getParams());

        SendMessageV3Response response = client.sendMessage(request);

        if (response == null) {
            log.debug("send fail: not response");
        } else if (!response.isSuccess()) {
            log.debug("send fail: {}", response.getCode());
            return false;
        }

        return true;
    }
}
