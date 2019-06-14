package net.guerlab.sms.server.service;

import net.guerlab.commons.number.NumberHelper;
import net.guerlab.commons.random.RandomUtil;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.exception.PhoneIsNullException;
import net.guerlab.sms.core.exception.RetryTimeShortException;
import net.guerlab.sms.server.entity.VerificationCode;
import net.guerlab.sms.server.properties.SmsProperties;
import net.guerlab.sms.server.repository.IVerificationCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 手机验证码服务实现
 *
 * @author guer
 *
 */
@Service
public class DefaultVerificationCodeService implements VerificationCodeService {

    private IVerificationCodeRepository repository;

    private SmsProperties properties;

    private NoticeService noticeService;

    private ICodeGenerate codeGenerate;

    @Autowired
    public void setRepository(IVerificationCodeRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setProperties(SmsProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Autowired
    public void setCodeGenerate(ICodeGenerate codeGenerate) {
        this.codeGenerate = codeGenerate;
    }

    @Override
    public String find(String phone, String identificationCode) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }

        phoneValidation(phone);

        VerificationCode verificationCode = repository.findOne(phone, identificationCode);

        return verificationCode == null ? null : verificationCode.getCode();
    }

    private String createIdentificationCode() {
        if (!properties.getVerificationCode().isUseIdentificationCode()) {
            return "";
        }

        return RandomUtil.nextString(properties.getVerificationCode().getIdentificationCodeLength());
    }

    @Override
    public void send(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new PhoneIsNullException();
        }

        String identificationCode = createIdentificationCode();

        phoneValidation(phone);

        VerificationCode verificationCode = repository.findOne(phone, identificationCode);

        boolean newVerificationCode = false;

        if (verificationCode == null) {
            verificationCode = new VerificationCode();
            verificationCode.setPhone(phone);
            verificationCode.setIdentificationCode(identificationCode);

            Long expirationTime = properties.getVerificationCode().getExpirationTime();
            Long retryIntervalTime = properties.getVerificationCode().getRetryIntervalTime();

            if (NumberHelper.greaterZero(expirationTime)) {
                verificationCode.setExpirationTime(LocalDateTime.now().plusSeconds(expirationTime));
            }
            if (NumberHelper.greaterZero(retryIntervalTime)) {
                verificationCode.setRetryTime(LocalDateTime.now().plusSeconds(retryIntervalTime));
            }

            verificationCode.setCode(codeGenerate.generate());
            newVerificationCode = true;
        } else {
            LocalDateTime retryTime = verificationCode.getRetryTime();

            if (retryTime != null) {
                long surplus =
                        retryTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

                if (surplus > 0) {
                    throw new RetryTimeShortException(surplus);
                }
            }
        }

        Map<String, String> params = new HashMap<>(2);
        params.put("code", verificationCode.getCode());
        params.put("identificationCode", verificationCode.getIdentificationCode());

        NoticeData notice = new NoticeData();
        notice.setType(VerificationCode.TYPE);
        notice.setParams(params);

        if (noticeService.send(notice, phone) && newVerificationCode) {
            repository.save(verificationCode);
        }
    }

    @Override
    public boolean verify(String phone, String code, String identificationCode) {
        if (StringUtils.isAnyBlank(phone, code)) {
            return false;
        }

        phoneValidation(phone);

        VerificationCode verificationCode = repository.findOne(phone, identificationCode);

        if (verificationCode == null) {
            return false;
        }

        boolean verifyData = Objects.equals(verificationCode.getCode(), code);

        if (verifyData && properties.getVerificationCode().isDeleteByVerifySucceed()) {
            repository.delete(phone, identificationCode);
        }

        if (!verifyData && properties.getVerificationCode().isDeleteByVerifyFail()) {
            repository.delete(phone, identificationCode);
        }

        return verifyData;
    }

    private void phoneValidation(String phone) {
        if (!noticeService.phoneRegValidation(phone)) {
            throw new PhoneIsNullException();
        }
    }

}
