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
