package net.guerlab.sms.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.utils.StringUtils;
import net.guerlab.sms.server.entity.VerificationCode;
import net.guerlab.sms.server.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@Slf4j
@Repository
@EnableConfigurationProperties(RedisProperties.class)
public class VerificationCodeRedisRepository implements IVerificationCodeRepository {

    private RedisProperties properties;

    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public void setProperties(RedisProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public VerificationCode findOne(String phone, String identificationCode) {
        String key = key(phone, identificationCode);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String json = operations.get(key);

        if (StringUtils.isBlank(json)) {
            log.debug("json data is empty");
            return null;
        }

        try {
            return objectMapper.readValue(json, VerificationCode.class);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void save(VerificationCode verificationCode) {
        String key = key(verificationCode.getPhone(), verificationCode.getIdentificationCode());

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        LocalDateTime expirationTime = verificationCode.getExpirationTime();

        String value;

        try {
            value = objectMapper.writeValueAsString(verificationCode);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if (expirationTime == null) {
            operations.set(key, value);
        } else {
            long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            long end = expirationTime.toEpochSecond(ZoneOffset.UTC);
            long timeout = end - now;

            operations.set(key, value, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void delete(String phone, String identificationCode) {
        redisTemplate.delete(key(phone, identificationCode));
    }

    private String key(String phone, String identificationCode) {
        String tempPrefix = StringUtils.trimToNull(properties.getKeyPrefix());

        String prefix;
        if (tempPrefix == null) {
            prefix = "";
        } else {
            prefix = tempPrefix + "_";
        }

        if (StringUtils.isBlank(identificationCode)) {
            return prefix + phone;
        }

        return prefix + phone + "_" + identificationCode;
    }

}
