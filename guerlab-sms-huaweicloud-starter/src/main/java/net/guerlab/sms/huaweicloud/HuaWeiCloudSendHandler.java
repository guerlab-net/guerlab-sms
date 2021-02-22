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
package net.guerlab.sms.huaweicloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.SendClientException;
import net.guerlab.sms.core.exception.SendFailedException;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.core.utils.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 华为云发送处理
 *
 * @author guer
 */
@Slf4j
public class HuaWeiCloudSendHandler implements SendHandler {

    /**
     * 无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
     */
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";

    /**
     * 无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
     */
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    private static final String DEFAULT_NATION_CODE = "+86";

    private final HuaWeiCloudProperties properties;

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient client;

    public HuaWeiCloudSendHandler(HuaWeiCloudProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
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
            if (!phone.startsWith("+")) {
                phone = DEFAULT_NATION_CODE + phone;
            }
            receiverBuilder.append(phone);
            receiverBuilder.append(",");
        }

        String receiver = receiverBuilder.substring(0, receiverBuilder.length() - 1);
        String templateParas = buildTemplateParas(params);
        String wsseHeader = buildWsseHeader();
        String body = buildRequestBody(receiver, templateId, templateParas);

        try {
            HttpResponse response = client.execute(RequestBuilder.create("POST").setUri(properties.getUri())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE).addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());

            String responseContent = EntityUtils.toString(response.getEntity());

            log.debug("responseContent: {}", responseContent);

            HuaWeiCloudResult result = objectMapper.readValue(responseContent, HuaWeiCloudResult.class);

            return HuaWeiCloudResult.SUCCESS_CODE.equals(result.getCode());
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

    private String buildRequestBody(String receiver, String templateId, String templateParas) {
        if (StringUtils.isAnyBlank(receiver, templateId)) {
            throw new SendFailedException("buildRequestBody(): receiver or templateId is null.");
        }

        String signature = StringUtils.trimToNull(properties.getSignature());

        List<NameValuePair> keyValues = new ArrayList<>();

        keyValues.add(new BasicNameValuePair("from", properties.getSender()));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (templateParas != null) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (signature != null) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }

        return URLEncodedUtils.format(keyValues, StandardCharsets.UTF_8);
    }

    /**
     * 构造X-WSSE参数值
     *
     * @return X-WSSE参数值
     */
    private String buildWsseHeader() {
        String appKey = properties.getAppKey();
        String appSecret = properties.getAppSecret();
        if (StringUtils.isAnyBlank(appKey, appSecret)) {
            throw new SendClientException("buildWsseHeader(): appKey or appSecret is null.");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);

        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes());

        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
}
