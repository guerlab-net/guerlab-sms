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

import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.NotFindSendHandlerException;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import net.guerlab.sms.server.service.NoticeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 多key测试
 *
 * @author guer
 */
public class MultiKeyTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.register(SmsConfiguration.class);
        context.register(TestHandlerAutoConfigure.class);
        context.refresh();
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void assertEquals() {
        NoticeService service = context.getBean(NoticeService.class);
        NoticeData noticeData = new NoticeData();
        noticeData.setType("foo");
        Assert.assertTrue(service.send(noticeData, "test"));
    }

    @Test(expected = NotFindSendHandlerException.class)
    public void assertNotEquals() {
        NoticeService service = context.getBean(NoticeService.class);
        NoticeData noticeData = new NoticeData();
        noticeData.setType("boo");
        service.send(noticeData, "test");
    }

    public static class TestHandlerAutoConfigure {

        @Bean
        @ConditionalOnBean(SmsSenderLoadBalancer.class)
        public TestSendHandler testSendHandler(SmsSenderLoadBalancer loadbalancer) {
            TestSendHandler handler = new TestSendHandler(Arrays.asList("test", "foo"));
            loadbalancer.addTarget(handler, true);
            loadbalancer.setWeight(handler, 1);
            return handler;
        }
    }

    private static class TestSendHandler implements SendHandler {

        private final List<String> acceptKeys;

        public TestSendHandler(List<String> acceptKeys) {
            this.acceptKeys = acceptKeys;
        }

        @Override
        public boolean send(NoticeData noticeData, Collection<String> phones) {
            return true;
        }

        @Override
        public boolean acceptSend(String type) {
            return acceptKeys.contains(type);
        }
    }
}
