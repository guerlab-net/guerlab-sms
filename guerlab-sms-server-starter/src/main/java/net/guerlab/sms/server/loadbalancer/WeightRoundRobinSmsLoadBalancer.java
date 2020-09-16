package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.WeightRoundRobinLoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * weight round robin Load Balancer
 *
 * @author guer
 */
public class WeightRoundRobinSmsLoadBalancer extends WeightRoundRobinLoadBalancer<SendHandler, NoticeData>
        implements SmsSenderLoadBalancer {

    public static final String TYPE_NAME = "WeightRoundRobin";
}

