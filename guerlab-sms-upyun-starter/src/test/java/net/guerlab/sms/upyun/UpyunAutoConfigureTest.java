package net.guerlab.sms.upyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.sms.server.loadbalancer.RandomSmsLoadBalancer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class UpyunAutoConfigureTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.registerBean("objectMapper", ObjectMapper.class);
        context.registerBean("smsSenderLoadbalancer", RandomSmsLoadBalancer.class);
        TestPropertyValues.of("sms.upyun.token=token").applyTo(context);
        TestPropertyValues.of("sms.upyun.templates.test=templateId").applyTo(context);
        TestPropertyValues.of("sms.upyun.params-orders.test=code").applyTo(context);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void testConditionalOnPropertyNull() {
        context.register(UpyunAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(UpyunSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.upyun.enable=").applyTo(context);
        context.register(UpyunAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(UpyunSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.upyun.enable=false").applyTo(context);
        context.register(UpyunAutoConfigure.class);
        context.refresh();
        context.getBean(UpyunSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.upyun.enable=true").applyTo(context);
        context.register(UpyunAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(UpyunSendHandler.class));
    }
}
