package net.guerlab.sms.server.autoconfigure;

import net.guerlab.commons.exception.ApplicationException;
import net.guerlab.sms.core.domain.NoticeInfo;
import net.guerlab.sms.core.domain.VerifyInfo;
import net.guerlab.sms.server.controller.SmsController;
import net.guerlab.sms.server.properties.SmsProperties;
import net.guerlab.sms.server.properties.SmsWebProperties;
import net.guerlab.sms.server.repository.IVerificationCodeRepository;
import net.guerlab.sms.server.repository.VerificationCodeMemoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 短信服务配置
 *
 * @author guer
 *
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ComponentScan({ "net.guerlab.sms.server.controller", "net.guerlab.sms.server.repository",
        "net.guerlab.sms.server.service"
})
public class SmsConfiguration {

    /**
     * 构造默认验证码储存接口实现
     *
     * @return 默认验证码储存接口实现
     */
    @Bean
    @ConditionalOnMissingBean(IVerificationCodeRepository.class)
    public IVerificationCodeRepository verificationCodeMemoryRepository() {
        return new VerificationCodeMemoryRepository();
    }

    /**
     * 设置控制器映射
     *
     * @param mapping
     *            RequestMappingHandlerMapping
     * @param smsProperties
     *            短信配置
     * @param controller
     *            短信Controller
     */
    @Autowired(required = false)
    @ConditionalOnBean({ RequestMappingHandlerMapping.class, SmsProperties.class })
    public void setWebMapping(RequestMappingHandlerMapping mapping, SmsProperties smsProperties,
            SmsController controller) {
        if (smsProperties.getWeb() == null || !smsProperties.getWeb().isEnable()) {
            return;
        }

        Method sendMethod;
        Method getMethod;
        Method verifyMethod;
        Method noticeMethod;

        try {
            sendMethod = SmsController.class.getMethod("sendVerificationCode", String.class);
            getMethod = SmsController.class.getMethod("getVerificationCode", String.class, String.class);
            verifyMethod = SmsController.class.getMethod("verifyVerificationCode", VerifyInfo.class);
            noticeMethod = SmsController.class.getMethod("sendNotice", NoticeInfo.class);
        } catch (Exception e) {
            throw new ApplicationException(e.getLocalizedMessage(), e);
        }

        SmsWebProperties webProperties = smsProperties.getWeb();
        String bathPath = getBasePath(webProperties);

        if (webProperties != null && webProperties.isEnableSend()) {
            RequestMappingInfo sendInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.POST).build();
            mapping.registerMapping(sendInfo, controller, sendMethod);
        }
        if (webProperties != null && webProperties.isEnableGet()) {
            RequestMappingInfo getInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.GET).produces("application/json").build();
            mapping.registerMapping(getInfo, controller, getMethod);
        }
        if (webProperties != null && webProperties.isEnableVerify()) {
            RequestMappingInfo verifyInfo = RequestMappingInfo.paths(bathPath + "/verificationCode")
                    .methods(RequestMethod.POST).build();
            mapping.registerMapping(verifyInfo, controller, verifyMethod);
        }
        if (webProperties != null && webProperties.isEnableNotice()) {
            RequestMappingInfo noticeInfo = RequestMappingInfo.paths(bathPath + "/notice").methods(RequestMethod.PUT)
                    .build();
            mapping.registerMapping(noticeInfo, controller, noticeMethod);
        }
    }

    private String getBasePath(SmsWebProperties properties) {
        if (properties == null) {
            return SmsWebProperties.DEFAULT_BASE_PATH;
        }

        String bathPath = properties.getBasePath();

        if (StringUtils.isBlank(bathPath)) {
            return SmsWebProperties.DEFAULT_BASE_PATH;
        }

        return bathPath.trim();
    }
}
