package net.guerlab.sms.server.annotation;

import net.guerlab.sms.server.autoconfigure.SmsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用短信服务
 *
 * @author guer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ SmsConfiguration.class })
public @interface EnableSmsServer {

}
