package net.guerlab.sms.yunpian;

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

public class YunPianAutoConfigureTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.registerBean("objectMapper", ObjectMapper.class);
        context.registerBean("smsSenderLoadbalancer", RandomSmsLoadBalancer.class);
        TestPropertyValues.of("sms.yunpian.apikey=apikey").applyTo(context);
        TestPropertyValues.of("sms.yunpian.templates.test=templateId").applyTo(context);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void testConditionalOnPropertyNull() {
        context.register(YunPianAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(YunPianSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.yunpian.enable=").applyTo(context);
        context.register(YunPianAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(YunPianSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.yunpian.enable=false").applyTo(context);
        context.register(YunPianAutoConfigure.class);
        context.refresh();
        context.getBean(YunPianSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.yunpian.enable=true").applyTo(context);
        context.register(YunPianAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(YunPianSendHandler.class));
    }
}
