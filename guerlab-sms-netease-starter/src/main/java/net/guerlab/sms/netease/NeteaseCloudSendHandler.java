package net.guerlab.sms.netease;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.server.utils.RandomUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.util.*;

/**
 * 网易云信发送处理
 *
 * @author guer
 */
@Slf4j
public class NeteaseCloudSendHandler implements SendHandler {

    /**
     * 请求路径URL
     */
    private static final String SERVER_URL = "https://api.netease.im/sms/sendtemplate.action";

    private final NeteaseCloudProperties properties;

    private final ObjectMapper objectMapper;

    private final CloseableHttpClient client;

    public NeteaseCloudSendHandler(NeteaseCloudProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        client = buildHttpclient();
    }

    private String buildStringArray(Collection<String> items) {
        boolean firstParam = true;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String item : items) {
            if (!firstParam) {
                builder.append(",");
            }
            builder.append("'");
            builder.append(item);
            builder.append("'");
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

        String nonce = RandomUtil.nextString(6);
        String paramsString = buildStringArray(params);
        String mobilesString = buildStringArray(phones);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(properties.getAppSecret(), nonce, curTime);

        HttpPost httpPost = new HttpPost(SERVER_URL);
        httpPost.addHeader("AppKey", properties.getAppKey());
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("templateid", templateId));
        nvps.add(new BasicNameValuePair("mobiles", mobilesString));
        nvps.add(new BasicNameValuePair("params", paramsString));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpResponse response = client.execute(httpPost);

            String responseContent = EntityUtils.toString(response.getEntity(), "utf-8");

            log.debug("responseContent: {}", responseContent);

            NeteaseCloudResult result = objectMapper.readValue(responseContent, NeteaseCloudResult.class);

            return NeteaseCloudResult.SUCCESS_CODE.equals(result.getCode());
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
}
