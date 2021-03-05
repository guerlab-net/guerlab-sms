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
package net.guerlab.sms.server.handler;

import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.server.entity.SmsSendEndEvent;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * 抽象发送处理
 *
 * @param <P>
 *         配置类类型
 * @author guer
 */
public abstract class AbstractSendHandler<P extends AbstractHandlerProperties<?>> implements SendHandler {

    protected final P properties;

    private final ApplicationEventPublisher eventPublisher;

    public AbstractSendHandler(P properties, ApplicationEventPublisher eventPublisher) {
        this.properties = properties;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean acceptSend(String type) {
        return properties.getTemplates().containsKey(type);
    }

    /**
     * 获取通道名称
     *
     * @return 通道名称
     */
    public abstract String getChannelName();

    /**
     * 发布发送结束事件
     *
     * @param noticeData
     *         通知内容
     * @param phones
     *         手机号列表
     */
    protected final void publishSendEndEvent(NoticeData noticeData, Collection<String> phones) {
        if (eventPublisher == null) {
            return;
        }
        eventPublisher.publishEvent(
                new SmsSendEndEvent(this, getChannelName(), phones, noticeData.getType(), noticeData.getParams()));
    }
}
