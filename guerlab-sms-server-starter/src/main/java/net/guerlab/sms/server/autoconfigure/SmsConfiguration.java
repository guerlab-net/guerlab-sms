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
package net.guerlab.sms.server.autoconfigure;

import net.guerlab.sms.core.domain.NoticeInfo;
import net.guerlab.sms.core.domain.VerifyInfo;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.controller.SmsController;
import net.guerlab.sms.server.loadbalancer.*;
import net.guerlab.sms.server.properties.*;
import net.guerlab.sms.server.repository.VerificationCodeMemoryRepository;
import net.guerlab.sms.server.repository.VerificationCodeRepository;
import net.guerlab.sms.server.service.SendAsyncThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@EnableConfigurationProperties({ SmsAsyncProperties.class, SmsProperties.class, SmsWebProperties.class,
        VerificationCodeProperties.class, VerificationCodeMemoryRepositoryProperties.class })
@ComponentScan({ "net.guerlab.sms.server.controller", "net.guerlab.sms.server.repository",
        "net.guerlab.sms.server.service" })
public class SmsConfiguration {

    private static String getBasePath(SmsWebProperties properties) {
        if (properties == null) {
            return SmsWebProperties.DEFAULT_BASE_PATH;
        }

        String bathPath = StringUtils.trimToNull(properties.getBasePath());

        return bathPath == null ? SmsWebProperties.DEFAULT_BASE_PATH : bathPath;
    }

    /**
     * 构造发送者负载均衡器
     *
     * @param properties
     *         短信配置
     * @return 发送者负载均衡器
     */
    @Bean
    @ConditionalOnMissingBean(SmsSenderLoadBalancer.class)
    public SmsSenderLoadBalancer smsSenderLoadBalancer(SmsProperties properties) {
        String type = properties.getLoadBalancerType();
        if (type == null) {
            return new RandomSmsLoadBalancer();
        }

        type = type.trim();

        if (RoundRobinSmsLoadBalancer.TYPE_NAME.equalsIgnoreCase(type)) {
            return new RoundRobinSmsLoadBalancer();
        } else if (WeightRandomSmsLoadBalancer.TYPE_NAME.equalsIgnoreCase(type)) {
            return new WeightRandomSmsLoadBalancer();
        } else if (WeightRoundRobinSmsLoadBalancer.TYPE_NAME.equalsIgnoreCase(type)) {
            return new WeightRoundRobinSmsLoadBalancer();
        } else {
            return new RandomSmsLoadBalancer();
        }
    }

    /**
     * 设置控制器映射
     *
     * @param mapping
     *         RequestMappingHandlerMapping
     * @param properties
     *         短信Web配置
     * @param controller
     *         短信Controller
     * @throws NoSuchMethodException
     *         if a matching method is not found
     *         or if the name is "&lt;init&gt;"or "&lt;clinit&gt;".
     * @throws SecurityException
     *         If a security manager, <i>s</i>, is present and
     *         the caller's class loader is not the same as or an
     *         ancestor of the class loader for the current class and
     *         invocation of {@link SecurityManager#checkPackageAccess
     *         s.checkPackageAccess()} denies access to the package
     *         of this class.
     */
    @Autowired(required = false)
    @ConditionalOnBean(RequestMappingHandlerMapping.class)
    public void setWebMapping(RequestMappingHandlerMapping mapping, SmsWebProperties properties,
            SmsController controller) throws NoSuchMethodException, SecurityException {
        if (properties == null || !properties.isEnable()) {
            return;
        }

        String bathPath = getBasePath(properties);

        if (properties.isEnableSend()) {
            Method sendMethod = SmsController.class.getMethod("sendVerificationCode", String.class);
            RequestMappingInfo sendInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.POST).build();
            mapping.registerMapping(sendInfo, controller, sendMethod);
        }
        if (properties.isEnableGet()) {
            Method getMethod = SmsController.class.getMethod("getVerificationCode", String.class, String.class);
            RequestMappingInfo getInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.GET).produces("application/json").build();
            mapping.registerMapping(getInfo, controller, getMethod);
        }
        if (properties.isEnableVerify()) {
            Method verifyMethod = SmsController.class.getMethod("verifyVerificationCode", VerifyInfo.class);
            RequestMappingInfo verifyInfo = RequestMappingInfo.paths(bathPath + "/verificationCode")
                    .methods(RequestMethod.POST).build();
            mapping.registerMapping(verifyInfo, controller, verifyMethod);
        }
        if (properties.isEnableNotice()) {
            Method noticeMethod = SmsController.class.getMethod("sendNotice", NoticeInfo.class);
            RequestMappingInfo noticeInfo = RequestMappingInfo.paths(bathPath + "/notice").methods(RequestMethod.PUT)
                    .build();
            mapping.registerMapping(noticeInfo, controller, noticeMethod);
        }
    }

    /**
     * 构造默认验证码储存接口实现
     *
     * @param properties
     *         验证码内存储存配置
     * @return 默认验证码储存接口实现
     */
    @Bean
    @ConditionalOnMissingBean(VerificationCodeRepository.class)
    public VerificationCodeRepository verificationCodeMemoryRepository(
            VerificationCodeMemoryRepositoryProperties properties) {
        VerificationCodeMemoryRepository repository = new VerificationCodeMemoryRepository();
        repository.setProperties(properties);
        return repository;
    }

    /**
     * 构造发送异步处理线程池
     *
     * @param properties
     *         短信异步配置
     * @return 发送异步处理线程池
     */
    @Bean
    @ConditionalOnProperty(name = "sms.async.enable", havingValue = "true")
    public SendAsyncThreadPoolExecutor sendAsyncThreadPoolExecutor(SmsAsyncProperties properties) {
        return new SendAsyncThreadPoolExecutor(properties);
    }
}
