package net.guerlab.sms.server.repository;

import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.entity.SmsSendEndEvent;
import net.guerlab.sms.server.handler.AbstractSendHandler;
import net.guerlab.sms.server.handler.SmsSendEndEventListener;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import net.guerlab.sms.server.service.NoticeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 事件测试
 *
 * @author guer
 */
@Slf4j
public class EventTest {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.register(SmsConfiguration.class);
        context.register(TestAutoConfigure.class);
        context.register(TestSmsSendEndEventListener.class);
        context.refresh();
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void assertEquals() {
        NoticeService noticeService = context.getBean(NoticeService.class);

        NoticeData noticeData = new NoticeData();
        noticeData.setType("test");
        noticeData.setParams(Collections.singletonMap("key", "value"));

        noticeService.send(noticeData, "test");

        Assert.assertEquals(threadLocal.get(), noticeData.getType());
    }

    @Test
    public void assertNotEquals() {
        NoticeService noticeService = context.getBean(NoticeService.class);

        NoticeData noticeData = new NoticeData();
        noticeData.setType("test");
        noticeData.setParams(Collections.singletonMap("key", "value"));

        noticeService.send(noticeData, "test");

        Assert.assertNotEquals(threadLocal.get(), "type");
    }

    @Component
    public static class TestSmsSendEndEventListener implements SmsSendEndEventListener {

        @SuppressWarnings("NullableProblems")
        @Override
        public void onApplicationEvent(SmsSendEndEvent event) {
            log.debug("Listener SmsSendEndEvent: {}", event);
            threadLocal.set(event.getType());
        }
    }

    @Configuration
    @EnableConfigurationProperties(TestProperties.class)
    @AutoConfigureAfter(SmsConfiguration.class)
    public static class TestAutoConfigure {

        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        @Bean
        @ConditionalOnBean(SmsSenderLoadBalancer.class)
        public TestSendHandler testSendHandler(TestProperties properties, SmsSenderLoadBalancer loadbalancer,
                ApplicationEventPublisher eventPublisher) {
            TestSendHandler handler = new TestSendHandler(properties, eventPublisher);
            loadbalancer.addTarget(handler, true);
            loadbalancer.setWeight(handler, properties.getWeight());
            return handler;
        }

    }

    public static class TestSendHandler extends AbstractSendHandler<TestProperties> {

        public TestSendHandler(TestProperties properties, ApplicationEventPublisher eventPublisher) {
            super(properties, eventPublisher);
        }

        @Override
        public boolean send(NoticeData noticeData, Collection<String> phones) {
            publishSendEndEvent(noticeData, phones);
            return true;
        }

        @Override
        public String getChannelName() {
            return "test";
        }
    }

    @ConfigurationProperties(prefix = "sms.test")
    public static class TestProperties extends AbstractHandlerProperties<String> {

        public TestProperties() {
            this.setTemplates(Collections.singletonMap("test", "test"));
        }
    }
}
