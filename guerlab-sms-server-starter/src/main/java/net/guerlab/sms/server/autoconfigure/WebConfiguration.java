package net.guerlab.sms.server.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * web自动配置
 *
 * @author guer
 *
 */
@Configuration
@Conditional(WebEnableCondition.class)
@ComponentScan({
        "net.guerlab.sms.server.controller"
})
public class WebConfiguration {
}
