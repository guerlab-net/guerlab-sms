package net.guerlab.sms.tester;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.aliyun.AliyunAutoConfigure;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.qcloud.QCloudAutoConfigure;
import net.guerlab.sms.server.loadbalancer.RandomSmsLoadBalancer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

@Slf4j
public class LoadbalancerTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.registerBean("objectMapper", ObjectMapper.class);
        context.registerBean("smsSenderLoadbalancer", RandomSmsLoadBalancer.class);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void loadbalancerTest() {
        TestPropertyValues.of("sms.aliyun.enable=true").applyTo(context);
        TestPropertyValues.of("sms.qcloud.enable=true").applyTo(context);
        context.register(AliyunAutoConfigure.class);
        context.register(QCloudAutoConfigure.class);
        context.refresh();
        RandomSmsLoadBalancer loadBalancer = context.getBean(RandomSmsLoadBalancer.class);
        SendHandler sendHandler = loadBalancer.choose();
        log.debug("loadBalancer: {}", loadBalancer);
        log.debug("loadBalancer choose: {}", sendHandler);
        Assert.assertNotNull(sendHandler);
    }
}
