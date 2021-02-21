package net.guerlab.sms.server.repository;

import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.loadbalancer.SmsSenderLoadBalancer;
import net.guerlab.sms.server.service.NoticeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * 自动配置测试
 *
 * @author guer
 */
public class AsyncTest {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static Thread runnerThread;

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.register(SmsConfiguration.class);
        context.register(TestHandlerAutoConfigure.class);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void enabled() {
        TestPropertyValues.of("sms.async.enable=true").applyTo(context);
        context.refresh();
        NoticeService service = context.getBean(NoticeService.class);
        NoticeData noticeData = new NoticeData();
        service.asyncSend(noticeData, "test");
        try {
            countDownLatch.await();
        } catch (Exception e) {
            // ignore
        }
        Assert.assertNotEquals(Thread.currentThread(), runnerThread);
    }

    @Test
    public void disabled() {
        TestPropertyValues.of("sms.async.enable=false").applyTo(context);
        context.refresh();
        NoticeService service = context.getBean(NoticeService.class);
        NoticeData noticeData = new NoticeData();
        service.asyncSend(noticeData, "test");
        try {
            countDownLatch.await();
        } catch (Exception e) {
            // ignore
        }
        Assert.assertEquals(Thread.currentThread(), runnerThread);
    }

    public static class TestHandlerAutoConfigure {

        @Bean
        @ConditionalOnBean(SmsSenderLoadBalancer.class)
        public TestSendHandler testSendHandler(SmsSenderLoadBalancer loadbalancer) {
            TestSendHandler handler = new TestSendHandler();
            loadbalancer.addTarget(handler, true);
            loadbalancer.setWeight(handler, 1);
            return handler;
        }
    }

    private static class TestSendHandler implements SendHandler {

        @Override
        public boolean send(NoticeData noticeData, Collection<String> phones) {
            runnerThread = Thread.currentThread();
            countDownLatch.countDown();
            return true;
        }
    }
}
