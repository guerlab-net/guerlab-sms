package net.guerlab.sms.server.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.guerlab.commons.exception.ApplicationException;
import net.guerlab.sms.server.entity.VerificationCode;

/**
 * 验证码redis储存实现
 *
 * @author guer
 *
 */
@Repository
public class VerificationCodeRedisRepository implements IVerificationCodeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeMemoryRepository.class);

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

        try {
            operations.set(key, objectMapper.writeValueAsString(verificationCode));
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ApplicationException(e);
        }

        LocalDateTime expirationTime = verificationCode.getExpirationTime();

        if (expirationTime != null) {
            Instant instant = expirationTime.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            redisTemplate.expireAt(key, date);
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