package net.guerlab.sms.server.repository;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import net.guerlab.sms.server.properties.RejectPolicy;
import net.guerlab.sms.server.properties.SmsAsyncProperties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 异步配置测试
 *
 * @author guer
 */
public class AsyncPropertiesTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext();
    }

    @After
    public void tearDown() {
        Optional.of(context).ifPresent(AnnotationConfigApplicationContext::close);
    }

    @Test
    public void enableWithTrue() {
        TestPropertyValues.of("sms.async.enable=true").applyTo(context);
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertTrue(properties.isEnable());
    }

    @Test
    public void enableWithFalse() {
        TestPropertyValues.of("sms.async.enable=false").applyTo(context);
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertFalse(properties.isEnable());
    }

    @Test
    public void defaultUnit() {
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertEquals(properties.getUnit(), TimeUnit.SECONDS);
    }

    @Test
    public void customerUnit() {
        TestPropertyValues.of("sms.async.unit=MINUTES").applyTo(context);
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertEquals(properties.getUnit(), TimeUnit.MINUTES);
    }

    @Test
    public void defaultRejectPolicy() {
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertEquals(properties.getRejectPolicy(), RejectPolicy.Abort);
    }

    @Test
    public void customerRejectPolicy() {
        TestPropertyValues.of("sms.async.reject-policy=Discard").applyTo(context);
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertEquals(properties.getRejectPolicy(), RejectPolicy.Discard);
    }

    @Test
    public void nullRejectPolicy() {
        TestPropertyValues.of("sms.async.reject-policy=").applyTo(context);
        context.register(SmsConfiguration.class);
        context.refresh();
        SmsAsyncProperties properties = context.getBean(SmsAsyncProperties.class);
        Assert.assertEquals(properties.getRejectPolicy(), RejectPolicy.Abort);
    }
}
