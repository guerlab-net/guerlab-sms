package net.guerlab.sms.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.commons.exception.ApplicationException;
import net.guerlab.sms.server.entity.VerificationCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 验证码redis储存实现
 *
 * @author guer
 *
 */
@Repository
public class VerificationCodeRedisRepository implements IVerificationCodeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeMemoryRepository.class);

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+0");

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public VerificationCode findOne(String phone, String identificationCode) {
        String key = key(phone, identificationCode);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String json = operations.get(key);

        if (StringUtils.isBlank(json)) {
            LOGGER.debug("json data is empty");
            return null;
        }

        VerificationCode verificationCode = null;
        try {
            verificationCode = objectMapper.readValue(json, VerificationCode.class);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
        }

        return verificationCode;
    }

    @Override
    public void save(VerificationCode verificationCode) {
        String key = key(verificationCode.getPhone(), verificationCode.getIdentificationCode());

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        LocalDateTime expirationTime = verificationCode.getExpirationTime();

        String value = null;

        try {
            value = objectMapper.writeValueAsString(verificationCode);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ApplicationException(e);
        }

        if (expirationTime == null) {
            operations.set(key, value);
        } else {
            long now = LocalDateTime.now().toEpochSecond(ZONE_OFFSET);
            long end = expirationTime.toEpochSecond(ZONE_OFFSET);
            long timeout = end - now;

            operations.set(key, value, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void delete(String phone, String identificationCode) {
        redisTemplate.delete(key(phone, identificationCode));
    }

    private String key(String phone, String identificationCode) {
        if (StringUtils.isBlank(identificationCode)) {
            return phone;
        }

        return phone + "_" + identificationCode;
    }

}