package net.guerlab.sms.aliyun;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.guerlab.commons.exception.ApplicationException;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

public class AliyunSendHandler implements SendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunSendHandler.class);

    private static final String OK = "OK";

    private static final String PRODUCT = "Dysmsapi";

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private AliyunProperties properties;

    private ObjectMapper objectMapper;

    private IAcsClient acsClient;

    public AliyunSendHandler(AliyunProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;

        initClient();
    }

    private void initClient() {
        String endPoint = properties.getEndpoint();
        String accessKeyId = properties.getAccessKeyId();
        String accessKeySecret = properties.getAccessKeySecret();

        IClientProfile profile = DefaultProfile.getProfile(endPoint, accessKeyId, accessKeySecret);

        try {
            DefaultProfile.addEndpoint(endPoint, endPoint, PRODUCT, DOMAIN);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ApplicationException(e);
        }

        acsClient = new DefaultAcsClient(profile);
    }

    @Override
    public void send(NoticeData noticeData, Collection<String> phones) {
        String paramString = null;
        try {
            paramString = objectMapper.writeValueAsString(noticeData.getParams());
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            return;
        }

        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(StringUtils.join(phones, ","));
        request.setSignName(properties.getSignName());
        request.setTemplateCode(properties.getTemplates(noticeData.getType()));
        request.setTemplateParam(paramString);

        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (!OK.equals(sendSmsResponse.getCode())) {
                LOGGER.debug("send fail[code={}, message={}]", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
            }
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
        }
    }
}