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
package net.guerlab.sms.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.handler.AbstractSendHandler;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * 阿里云短信发送处理
 *
 * @author guer
 */
@Slf4j
public class AliyunSendHandler extends AbstractSendHandler<AliyunProperties> {

    private static final String OK = "OK";

    private static final String PRODUCT = "Dysmsapi";

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private final ObjectMapper objectMapper;

    private final IAcsClient acsClient;

    /**
     * 构造阿里云短信发送处理
     *
     * @param properties
     *         阿里云短信配置
     * @param eventPublisher
     *         spring应用事件发布器
     * @param objectMapper
     *         objectMapper
     */
    public AliyunSendHandler(AliyunProperties properties, ApplicationEventPublisher eventPublisher,
            ObjectMapper objectMapper) {
        super(properties, eventPublisher);
        this.objectMapper = objectMapper;

        String endPoint = properties.getEndpoint();
        String accessKeyId = properties.getAccessKeyId();
        String accessKeySecret = properties.getAccessKeySecret();

        IClientProfile profile = DefaultProfile.getProfile(endPoint, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(endPoint, PRODUCT, DOMAIN);

        acsClient = new DefaultAcsClient(profile);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String paramString;
        try {
            paramString = objectMapper.writeValueAsString(noticeData.getParams());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return false;
        }

        SendSmsRequest request = new SendSmsRequest();
        request.setSysMethod(MethodType.POST);
        request.setPhoneNumbers(StringUtils.join(phones, ","));
        request.setSignName(properties.getSignName());
        request.setTemplateCode(properties.getTemplates(noticeData.getType()));
        request.setTemplateParam(paramString);

        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            if (OK.equals(sendSmsResponse.getCode())) {
                publishSendEndEvent(noticeData, phones);
                return true;
            }

            log.debug("send fail[code={}, message={}]", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        return false;
    }

    @Override
    public String getChannelName() {
        return "aliyun";
    }
}
