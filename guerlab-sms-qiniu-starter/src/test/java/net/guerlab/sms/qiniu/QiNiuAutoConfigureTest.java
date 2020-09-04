package net.guerlab.sms.qiniu;

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

public class QiNiuAutoConfigureTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.registerBean("objectMapper", ObjectMapper.class);
        context.registerBean("smsSenderLoadbalancer", RandomSmsLoadBalancer.class);
        TestPropertyValues.of("sms.qiniu.access-key=accessKey").applyTo(context);
        TestPropertyValues.of("sms.qiniu.secret-key=secretKey").applyTo(context);
        TestPropertyValues.of("sms.qiniu.templates.test=templateId").applyTo(context);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void testConditionalOnPropertyNull() {
        context.register(QiNiuAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(QiNiuSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.qiniu.enable=").applyTo(context);
        context.register(QiNiuAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(QiNiuSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.qiniu.enable=false").applyTo(context);
        context.register(QiNiuAutoConfigure.class);
        context.refresh();
        context.getBean(QiNiuSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.qiniu.enable=true").applyTo(context);
        context.register(QiNiuAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(QiNiuSendHandler.class));
    }
}
