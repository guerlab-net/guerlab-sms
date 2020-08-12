package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.ILoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * @author guer
 */
public interface SmsSenderLoadBalancer extends ILoadBalancer<SendHandler, NoticeData> {}
