/*
 * Copyright 2018-2021 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

