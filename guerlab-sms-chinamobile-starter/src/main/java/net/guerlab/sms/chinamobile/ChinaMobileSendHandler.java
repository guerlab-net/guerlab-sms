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
package net.guerlab.sms.chinamobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.SendFailedException;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.handler.AbstractSendHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 移动云发送处理
 *
 * @author guer
 */
@Slf4j
public class ChinaMobileSendHandler extends AbstractSendHandler<ChinaMobileProperties> {

    private static final String BODY_TEMPLATE = "{\"ecName\":\"%s\",\"apId\":\"%s\",\"templateId\":\"%s\",\"mobiles\":\"%s\",\"params\":\"%s\",\"sign\":\"%s\",\"addSerial\":\"\",\"mac\":\"%s\"}";

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient client;

    public ChinaMobileSendHandler(ChinaMobileProperties properties, ObjectMapper objectMapper) {
        super(properties);
        this.objectMapper = objectMapper;
        client = buildHttpclient();
    }

    /**
     * 构造模板参数
     *
     * @param params
     *         参数列表
     * @return 模板参数
     */
    private static String buildTemplateParas(Collection<String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        boolean firstParam = true;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String param : params) {
            if (!firstParam) {
                builder.append(",");
            }
            builder.append("\"");
            builder.append(param);
            builder.append("\"");
            firstParam = false;
        }
        builder.append("]");

        return builder.toString();
    }

    private static String buildMac(String ecName, String apId, String secretKey, String templateId, String mobiles,
            String params, String sign) {
        String origin = ecName + apId + secretKey + templateId + mobiles + params + sign;
        return DigestUtils.md5Hex(origin);
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

        StringBuilder receiverBuilder = new StringBuilder();
        for (String phone : phones) {
            if (StringUtils.isBlank(phone)) {
                continue;
            }
            receiverBuilder.append(phone.trim());
            receiverBuilder.append(",");
        }

        String mobiles = receiverBuilder.substring(0, receiverBuilder.length() - 1);
        String paramsString = buildTemplateParas(params);
        String body = buildRequestBody(mobiles, templateId, paramsString);

        try {
            HttpResponse response = client.execute(
                    RequestBuilder.create("POST").setUri(properties.getUri()).setEntity(new StringEntity(body))
                            .build());

            String responseContent = EntityUtils.toString(response.getEntity());

            log.debug("responseContent: {}", responseContent);

            ChinaMobileResult result = objectMapper.readValue(responseContent, ChinaMobileResult.class);

            return ChinaMobileResult.SUCCESS_RSPCOD.equals(result.getRspcod());
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

    private String buildRequestBody(String mobiles, String templateId, String paramsString) {
        if (StringUtils.isAnyBlank(mobiles, templateId)) {
            throw new SendFailedException("buildRequestBody(): mobiles or templateId is null.");
        }

        String ecName = StringUtils.trimToNull(properties.getEcName());
        String apId = StringUtils.trimToNull(properties.getApId());
        String secretKey = StringUtils.trimToNull(properties.getSecretKey());
        String sign = StringUtils.trimToNull(properties.getSign());
        String mac = buildMac(ecName, apId, secretKey, templateId, mobiles, paramsString, sign);

        String body = String
                .format(BODY_TEMPLATE, ecName, apId, templateId, mobiles, paramsString.replace("\"", "\\\""), sign,
                        mac);

        return Base64.encodeBase64String(body.getBytes(StandardCharsets.UTF_8));
    }
}
