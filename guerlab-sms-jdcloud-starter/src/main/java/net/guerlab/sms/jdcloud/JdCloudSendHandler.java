package net.guerlab.sms.jdcloud;

import com.jdcloud.sdk.auth.CredentialsProvider;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import com.jdcloud.sdk.service.sms.model.BatchSendRequest;
import com.jdcloud.sdk.service.sms.model.BatchSendResult;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 京东云发送处理
 *
 * @author guer
 */
@Slf4j
public class JdCloudSendHandler implements SendHandler {

    private final JdCloudProperties properties;

    private final SmsClient smsClient;

    public JdCloudSendHandler(JdCloudProperties properties) {
        this.properties = properties;
        CredentialsProvider credentialsProvider = new StaticCredentialsProvider(properties.getAccessKeyId(),
                properties.getSecretAccessKey());
        smsClient = SmsClient.builder().credentialsProvider(credentialsProvider)
                .httpRequestConfig(new HttpRequestConfig.Builder().protocol(Protocol.HTTP).build()).build();
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        String templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        List<String> paramsOrder = properties.getParamsOrder(type);

        ArrayList<String> params = new ArrayList<>();

        if (!paramsOrder.isEmpty()) {
            Map<String, String> paramMap = noticeData.getParams();
            for (String paramName : paramsOrder) {
                String paramValue = paramMap.get(paramName);

                params.add(paramValue);
            }
        }

        BatchSendRequest request = new BatchSendRequest();
        request.setRegionId(properties.getRegion());
        request.setTemplateId(templateId);
        request.setSignId(properties.getSignId());
        request.setPhoneList(new ArrayList<>(phones));
        request.setParams(params);
        BatchSendResult result = smsClient.batchSend(request).getResult();
        Boolean status = result.getStatus();
        boolean flag = status != null && status;

        if (!flag) {
            log.debug("send fail, error info: [{}:{}]", result.getCode(), result.getMessage());
        }

        return flag;
    }
}
