package net.guerlab.sms.baiducloud;

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

public class BaiduCloudAutoConfigureTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
        context.registerBean("objectMapper", ObjectMapper.class);
        context.registerBean("smsSenderLoadbalancer", RandomSmsLoadBalancer.class);
        TestPropertyValues.of("sms.baiducloud.access-key-id=accessKeyId").applyTo(context);
        TestPropertyValues.of("sms.baiducloud.secret-access-key=secretAccessKey").applyTo(context);
        TestPropertyValues.of("sms.baiducloud.endpoint=endpoint").applyTo(context);
        TestPropertyValues.of("sms.baiducloud.signature-id=signatureId").applyTo(context);
        TestPropertyValues.of("sms.baiducloud.templates.test=templateId").applyTo(context);
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void testConditionalOnPropertyNull() {
        context.register(BaiduCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(BaiduCloudSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.baiducloud.enable=").applyTo(context);
        context.register(BaiduCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(BaiduCloudSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.baiducloud.enable=false").applyTo(context);
        context.register(BaiduCloudAutoConfigure.class);
        context.refresh();
        context.getBean(BaiduCloudSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.baiducloud.enable=true").applyTo(context);
        context.register(BaiduCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(BaiduCloudSendHandler.class));
    }
}
