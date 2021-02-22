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
package net.guerlab.sms.server.repository;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import net.guerlab.sms.server.service.CodeGenerate;
import net.guerlab.sms.server.service.SendAsyncThreadPoolExecutor;
import net.guerlab.sms.server.service.VerificationCodeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

/**
 * 自动配置测试
 *
 * @author guer
 */
public class AutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.register(SmsConfiguration.class);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void smsSenderLoadBalancer() {
        context.refresh();
        Assert.assertNotNull(context.getBean(SmsSenderLoadBalancer.class));
    }

    @Test
    public void codeGenerate() {
        context.refresh();
        Assert.assertNotNull(context.getBean(CodeGenerate.class));
    }

    @Test
    public void verificationCodeRepository() {
        context.refresh();
        Assert.assertNotNull(context.getBean(VerificationCodeRepository.class));
    }

    @Test
    public void verificationCodeService() {
        context.refresh();
        Assert.assertNotNull(context.getBean(VerificationCodeService.class));
    }

    @Test
    public void sendAsyncThreadPoolExecutorIsNotNull() {
        TestPropertyValues.of("sms.async.enable=true").applyTo(context);
        context.refresh();
        Assert.assertNotNull(context.getBean(SendAsyncThreadPoolExecutor.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void sendAsyncThreadPoolExecutorIsNull() {
        TestPropertyValues.of("sms.async.enable=false").applyTo(context);
        context.refresh();
        context.getBean(SendAsyncThreadPoolExecutor.class);
    }
}
