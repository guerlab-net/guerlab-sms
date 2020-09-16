package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.RoundRobinLoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * round robin Load Balancer
 *
 * @author guer
 */
public class RoundRobinSmsLoadBalancer extends RoundRobinLoadBalancer<SendHandler, NoticeData>
        implements SmsSenderLoadBalancer {

    public static final String TYPE_NAME = "RoundRobin";
}

