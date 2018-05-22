package net.guerlab.sms.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;

/**
 * 启用短信服务
 *
 * @author guer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SmsConfiguration.class)
public @interface EnableSmsServer {

}
