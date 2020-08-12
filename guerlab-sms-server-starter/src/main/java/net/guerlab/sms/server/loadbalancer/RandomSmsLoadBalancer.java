package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.RandomLoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * @author guer
 */
public class RandomSmsLoadBalancer extends RandomLoadBalancer<SendHandler, NoticeData>
        implements SmsSenderLoadBalancer {}
