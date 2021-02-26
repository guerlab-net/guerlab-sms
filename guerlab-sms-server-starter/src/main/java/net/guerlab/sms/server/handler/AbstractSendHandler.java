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

import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.server.properties.AbstractHandlerProperties;

/**
 * 抽象发送处理
 *
 * @param <P>
 *         配置类类型
 * @author guer
 */
public abstract class AbstractSendHandler<P extends AbstractHandlerProperties<?>> implements SendHandler {

    protected final P properties;

    public AbstractSendHandler(P properties) {
        this.properties = properties;
    }

    @Override
    public boolean acceptSend(String type) {
        return properties.getTemplates().containsKey(type);
    }
}
