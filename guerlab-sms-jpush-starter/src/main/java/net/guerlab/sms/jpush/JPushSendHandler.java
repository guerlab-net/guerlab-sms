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
package net.guerlab.sms.jpush;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.server.handler.AbstractSendHandler;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

/**
 * 极光发送处理
 *
 * @author guer
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Slf4j
public class JPushSendHandler extends AbstractSendHandler<JPushProperties> {

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient client;

    public JPushSendHandler(JPushProperties properties, ObjectMapper objectMapper) {
        super(properties);
        this.objectMapper = objectMapper;
        client = buildHttpclient();
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        Integer templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        String[] phoneArray = phones.toArray(new String[] {});

        try {
            if (phoneArray.length > 1) {
                MultiRecipient data = new MultiRecipient();
                data.setSignId(properties.getSignId());
                data.setTempId(templateId);

                ArrayList<Recipient> recipients = new ArrayList<>(phoneArray.length);
                for (String phone : phoneArray) {
                    Recipient recipient = new Recipient();
                    recipient.setMobile(phone);
                    recipient.setTempPara(noticeData.getParams());
                    recipients.add(recipient);
                }

                data.setRecipients(recipients);

                MultiResult result = getResponse("https://api.sms.jpush.cn/v1/messages/batch", data, MultiResult.class);
                return result.getSuccessCount() > 0;
            } else {
                Recipient data = new Recipient();
                data.setMobile(phoneArray[0]);
                data.setSignId(properties.getSignId());
                data.setTempId(templateId);
                data.setTempPara(noticeData.getParams());

                SingleResult result = getResponse("https://api.sms.jpush.cn/v1/messages", data, SingleResult.class);
                return result.getError() == null;
            }
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage(), e);
        }

        return false;
    }

    private <T> T getResponse(String uri, Object requestData, Class<T> clazz) throws Exception {
        HttpResponse response = client.execute(
                RequestBuilder.create("POST").setUri(uri).addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .addHeader(HttpHeaders.AUTHORIZATION, "Basic " + getSign())
                        .setEntity(new StringEntity(objectMapper.writeValueAsString(requestData))).build());

        String responseContent = EntityUtils.toString(response.getEntity());

        log.debug("responseContent: {}", responseContent);

        return objectMapper.readValue(responseContent, clazz);
    }

    private String getSign() {
        String origin = properties.getAppKey() + ":" + properties.getMasterSecret();
        return Base64.getEncoder().encodeToString(origin.getBytes(StandardCharsets.UTF_8));
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
}
