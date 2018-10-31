package net.guerlab.sms.server.autoconfigure;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import net.guerlab.commons.exception.ApplicationException;
import net.guerlab.sms.core.domain.NoticeInfo;
import net.guerlab.sms.core.domain.VerifyInfo;
import net.guerlab.sms.server.controller.SmsController;
import net.guerlab.sms.server.properties.SmsProperties;
import net.guerlab.sms.server.properties.SmsWebProperties;
import net.guerlab.sms.server.repository.IVerificationCodeRepository;
import net.guerlab.sms.server.repository.VerificationCodeMemoryRepository;

/**
 * 短信服务配置
 *
 * @author guer
 *
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ComponentScan({
        "net.guerlab.sms.server.controller", "net.guerlab.sms.server.repository", "net.guerlab.sms.server.service"
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
    @Autowired
    public void setWebMapping(RequestMappingHandlerMapping mapping, SmsProperties smsProperties,
            SmsController controller) {
        if (smsProperties.getWeb() == null || !smsProperties.getWeb().isEnable()) {
            return;
        }

        Method sendMethod = null;
        Method getMethod = null;
        Method verifyMethod = null;
        Method noticeMethod = null;

        try {
            sendMethod = SmsController.class.getMethod("sendVerificationCode", String.class);
            getMethod = SmsController.class.getMethod("getVerificationCode", String.class, String.class);
            verifyMethod = SmsController.class.getMethod("verifyVerificationCode", VerifyInfo.class);
            noticeMethod = SmsController.class.getMethod("sendNotice", NoticeInfo.class);
        } catch (Exception e) {
            throw new ApplicationException(e.getLocalizedMessage(), e);
        }

        String bathPath = getBasePath(smsProperties);

        RequestMappingInfo sendInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                .methods(RequestMethod.POST).build();
        RequestMappingInfo getInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                .methods(RequestMethod.GET).produces("application/json").build();
        RequestMappingInfo verifyInfo = RequestMappingInfo.paths(bathPath + "/verificationCode")
                .methods(RequestMethod.POST).build();
        RequestMappingInfo noticeInfo = RequestMappingInfo.paths(bathPath + "/notice").methods(RequestMethod.PUT)
                .build();

        mapping.registerMapping(sendInfo, controller, sendMethod);
        mapping.registerMapping(getInfo, controller, getMethod);
        mapping.registerMapping(verifyInfo, controller, verifyMethod);
        mapping.registerMapping(noticeInfo, controller, noticeMethod);
    }

    private String getBasePath(SmsProperties smsProperties) {
        SmsWebProperties properties = smsProperties.getWeb();

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
