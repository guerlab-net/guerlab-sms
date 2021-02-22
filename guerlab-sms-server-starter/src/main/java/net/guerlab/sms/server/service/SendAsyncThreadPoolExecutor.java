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
package net.guerlab.sms.server.service;

import net.guerlab.sms.server.properties.RejectPolicy;
import net.guerlab.sms.server.properties.SmsAsyncProperties;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发送异步处理线程池
 *
 * @author guer
 */
public class SendAsyncThreadPoolExecutor {

    private final ThreadPoolExecutor executor;

    public SendAsyncThreadPoolExecutor(SmsAsyncProperties properties) {
        executor = new ThreadPoolExecutor(properties.getCorePoolSize(), properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(), properties.getUnit(),
                new LinkedBlockingQueue<>(properties.getQueueCapacity()), new DefaultThreadFactory(),
                buildRejectedExecutionHandler(properties.getRejectPolicy()));
    }

    private static RejectedExecutionHandler buildRejectedExecutionHandler(RejectPolicy type) {
        if (type == null) {
            return new ThreadPoolExecutor.AbortPolicy();
        }
        switch (type) {
            case Caller:
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case Discard:
                return new ThreadPoolExecutor.DiscardPolicy();
            case DiscardOldest:
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            default:
                return new ThreadPoolExecutor.AbortPolicy();
        }
    }

    /**
     * 提交异步任务
     *
     * @param command
     *         待执行任务
     */
    public void submit(Runnable command) {
        if (command == null) {
            return;
        }
        executor.execute(command);
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "sms-send-async-thread-";
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
            thread.setDaemon(false);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
}
