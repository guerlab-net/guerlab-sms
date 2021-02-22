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
package net.guerlab.sms.server.repository;

import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.server.entity.VerificationCode;
import net.guerlab.sms.server.properties.VerificationCodeMemoryRepositoryProperties;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author guer
 */
@Slf4j
public class VerificationCodeMemoryRepositoryTest {

    public static void main(String[] args) {
        VerificationCodeMemoryRepositoryProperties properties = new VerificationCodeMemoryRepositoryProperties();
        properties.setGcFrequency(30L);

        VerificationCodeMemoryRepository repository = new VerificationCodeMemoryRepository();
        repository.setProperties(properties);

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("test-add");
            return thread;
        });

        executor.scheduleAtFixedRate(() -> {
            LocalDateTime now = LocalDateTime.now();
            VerificationCode verificationCode = new VerificationCode();

            verificationCode.setPhone(now.toString());
            verificationCode.setExpirationTime(now);

            repository.save(verificationCode);
        }, 1, 1, TimeUnit.SECONDS);

    }
}
