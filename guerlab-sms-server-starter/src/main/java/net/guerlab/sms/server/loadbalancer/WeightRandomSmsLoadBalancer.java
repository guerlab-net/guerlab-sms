package net.guerlab.sms.server.loadbalancer;

import net.guerlab.loadbalancer.WeightRandomLoadBalancer;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

/**
 * weight random Load Balancer
 *
 * @author guer
 */
public class WeightRandomSmsLoadBalancer extends WeightRandomLoadBalancer<SendHandler, NoticeData>
        implements SmsSenderLoadBalancer {

    public static final String TYPE_NAME = "WeightRandom";
}
