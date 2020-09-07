package net.guerlab.sms.huaweicloud;

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

public class HuaWeiCloudAutoConfigureTest {

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
    public void testConditionalOnPropertyNull() {
        context.register(HuaWeiCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(HuaWeiCloudSendHandler.class));
    }

    @Test
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.huawei.enable=").applyTo(context);
        context.register(HuaWeiCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(HuaWeiCloudSendHandler.class));
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.huawei.enable=false").applyTo(context);
        context.register(HuaWeiCloudAutoConfigure.class);
        context.refresh();
        context.getBean(HuaWeiCloudSendHandler.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.huawei.enable=true").applyTo(context);
        context.register(HuaWeiCloudAutoConfigure.class);
        context.refresh();
        Assert.assertNotNull(context.getBean(HuaWeiCloudSendHandler.class));
    }
}
