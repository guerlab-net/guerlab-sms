package net.guerlab.sms.server.autoconfigure;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * web启用判断
 *
 * @author guer
 *
 */
public class WebEnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty("sms.enable-web", Boolean.TYPE, false);
    }

}