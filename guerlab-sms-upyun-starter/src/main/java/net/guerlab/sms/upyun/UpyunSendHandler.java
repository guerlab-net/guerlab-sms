/*
 * Copyright 2018-2021 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.guerlab.sms.upyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.handler.AbstractSendHandler;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationEventPublisher;

import javax.net.ssl.SSLContext;
import java.util.*;

/**
 * 又拍云发送处理
 *
 * @author guer
 */
@Slf4j
public class UpyunSendHandler extends AbstractSendHandler<UpyunProperties> {

    private static final String API_URL = "https://sms-api.upyun.com/api/messages";

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient client;

    public UpyunSendHandler(UpyunProperties properties, ApplicationEventPublisher eventPublisher,
            ObjectMapper objectMapper) {
        super(properties, eventPublisher);
        this.objectMapper = objectMapper;
        client = buildHttpclient();
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

        UpyunSendRequest request = new UpyunSendRequest();
        request.setMobile(StringUtils.join(phones, ","));
        request.setTemplateId(templateId);
        request.setVars(StringUtils.join(params, "|"));

        try {
            HttpResponse response = client.execute(
                    RequestBuilder.post(API_URL).addHeader(HttpHeaders.AUTHORIZATION, properties.getToken())
                            .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()).setEntity(new StringEntity(objectMapper.writeValueAsString(request))).build());

            String responseContent = EntityUtils.toString(response.getEntity());

            boolean isJson = responseContent.startsWith("{") && responseContent.endsWith("}");
            boolean sendFail = !responseContent.contains("message_ids");
            if (!isJson || sendFail) {
                log.debug("send fail: {}", responseContent);
                return false;
            }

            log.debug("responseContent: {}", responseContent);

            UpyunSendResult result = objectMapper.readValue(responseContent, UpyunSendResult.class);

            Collection<MessageId> messageIds = result.getMessageIds();

            if (messageIds == null || messageIds.isEmpty()) {
                return false;
            }

            boolean succeed = messageIds.stream().filter(Objects::nonNull).anyMatch(MessageId::succeed);

            if (succeed) {
                publishSendEndEvent(noticeData, phones);
            }

            return succeed;
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private CloseableHttpClient buildHttpclient() {
        try {
            TrustStrategy trustStrategy = (x509CertChain, authType) -> true;
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStrategy).build();

            return HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String getChannelName() {
        return "upyun";
    }
}
