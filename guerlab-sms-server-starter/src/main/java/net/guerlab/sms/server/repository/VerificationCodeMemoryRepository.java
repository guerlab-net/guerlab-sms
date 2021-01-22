package net.guerlab.sms.server.repository;

import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.entity.VerificationCode;
import net.guerlab.sms.server.properties.VerificationCodeMemoryRepositoryProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 验证码内存储存实现
 *
 * @author guer
 */
@Slf4j
public class VerificationCodeMemoryRepository implements IVerificationCodeRepository {

    private final Map<String, VerificationCode> cache = new ConcurrentHashMap<>();

    private final ScheduledThreadPoolExecutor gcScheduledExecutor = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("VerificationCodeMemoryRepository-GC");
        return thread;
    });

    private final Runnable task = this::gcHandler;

    private VerificationCodeMemoryRepositoryProperties properties;

    @Override
    public VerificationCode findOne(String phone, String identificationCode) {
        String key = key(phone, identificationCode);
        VerificationCode verificationCode = cache.get(key);

        if (verificationCode == null) {
            log.debug("verificationCode is null, key: {}", key);
            return null;
        }

        LocalDateTime expirationTime = verificationCode.getExpirationTime();
        if (expirationTime != null && expirationTime.isBefore(LocalDateTime.now())) {
            log.debug("verificationCode is not null, but timeout, key: {}", key);
            cache.remove(key);
            return null;
        }

        return verificationCode;
    }

    @Override
    public void save(VerificationCode verificationCode) {
        String key = key(verificationCode.getPhone(), verificationCode.getIdentificationCode());

        cache.put(key, verificationCode);
    }

    @Override
    public void delete(String phone, String identificationCode) {
        cache.remove(key(phone, identificationCode));
    }

    public void setProperties(VerificationCodeMemoryRepositoryProperties properties) {
        this.properties = properties;
        initGcThread();
    }

    private String key(String phone, String identificationCode) {
        if (StringUtils.isBlank(identificationCode)) {
            return phone;
        }

        return phone + "_" + identificationCode;
    }

    private void initGcThread() {
        long gcFrequency = properties.getGcFrequency();
        if (gcFrequency <= 0) {
            gcFrequency = VerificationCodeMemoryRepositoryProperties.DEFAULT_GC_FREQUENCY;
        }

        gcScheduledExecutor.remove(task);
        gcScheduledExecutor.scheduleAtFixedRate(task, gcFrequency, gcFrequency, TimeUnit.SECONDS);
    }

    private void gcHandler() {
        LocalDateTime now = LocalDateTime.now();

        boolean debug = log.isDebugEnabled();
        Set<String> keys = cache.keySet();
        List<String> removeKeys = debug ? new ArrayList<>(keys.size()) : null;

        keys.forEach(key -> {
            VerificationCode verificationCode = cache.get(key);
            if (verificationCode != null) {
                LocalDateTime expirationTime = verificationCode.getExpirationTime();
                if (expirationTime != null && expirationTime.isBefore(now)) {
                    cache.remove(key);
                    if (debug) {
                        removeKeys.add(key);
                    }
                }
            }
        });

        if (debug) {
            log.debug("gc remove keys: {}", removeKeys.size());
        }
    }
}
