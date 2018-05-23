package net.guerlab.sms.server.repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.guerlab.sms.server.entity.VerificationCode;

/**
 * 验证码内存储存实现
 *
 * @author guer
 *
 */
public class VerificationCodeMemoryRepository implements IVerificationCodeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeMemoryRepository.class);

    private Map<String, VerificationCode> cache = new ConcurrentHashMap<>();

    @Override
    public VerificationCode findOne(String phone, String identificationCode) {
        String key = key(phone, identificationCode);
        VerificationCode verificationCode = cache.get(key);

        if (verificationCode == null) {
            LOGGER.debug("verificationCode is null");
            return null;
        }

        LocalDateTime expirationTime = verificationCode.getExpirationTime();
        if (expirationTime != null && expirationTime.isBefore(LocalDateTime.now())) {
            LOGGER.debug("verificationCode is not null, but timeout");
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

    private String key(String phone, String identificationCode) {
        if (StringUtils.isBlank(identificationCode)) {
            return phone;
        }

        return phone + "_" + identificationCode;
    }

}
