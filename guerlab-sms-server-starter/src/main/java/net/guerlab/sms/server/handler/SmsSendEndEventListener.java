package net.guerlab.sms.server.handler;

import net.guerlab.sms.server.entity.SmsSendEndEvent;
import org.springframework.context.ApplicationListener;

/**
 * 发送结束事件监听接口
 *
 * @author guer
 */
public interface SmsSendEndEventListener extends ApplicationListener<SmsSendEndEvent> {}
