package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.ILoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * 短信发送负载均衡
 *
 * @author guer
 */
public interface SmsSenderLoadBalancer extends ILoadBalancer<SendHandler, NoticeData> {}
