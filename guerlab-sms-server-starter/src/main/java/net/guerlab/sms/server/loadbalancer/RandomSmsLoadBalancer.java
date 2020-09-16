package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.RandomLoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * random Load Balancer
 *
 * @author guer
 */
public class RandomSmsLoadBalancer extends RandomLoadBalancer<SendHandler, NoticeData>
        implements SmsSenderLoadBalancer {

    public static final String TYPE_NAME = "Random";
}
