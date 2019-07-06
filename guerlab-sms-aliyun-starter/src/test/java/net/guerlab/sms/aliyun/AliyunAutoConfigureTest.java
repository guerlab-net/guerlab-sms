package net.guerlab.sms.aliyun;

import net.guerlab.sms.server.properties.SmsProperties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AliyunAutoConfigureTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyNull() {
        context.register(AliyunAutoConfigure.class);
        context.refresh();
        context.getBean(AliyunAutoConfigure.class);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyEmpty() {
        TestPropertyValues.of("sms.aliyun.enable=").applyTo(context);
        context.register(AliyunAutoConfigure.class);
        context.refresh();
        context.getBean(AliyunAutoConfigure.class);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditionalOnPropertyFalse() {
        TestPropertyValues.of("sms.aliyun.enable=false").applyTo(context);
        context.register(AliyunAutoConfigure.class);
        context.refresh();
        context.getBean(AliyunAutoConfigure.class);
    }

    @Test
    public void testConditionalOnPropertyTrue() {
        TestPropertyValues.of("sms.aliyun.enable=true").applyTo(context);
        context.register(AliyunAutoConfigure.class);
        context.refresh();
        assertNotNull(context.getBean(AliyunAutoConfigure.class));
    }
}