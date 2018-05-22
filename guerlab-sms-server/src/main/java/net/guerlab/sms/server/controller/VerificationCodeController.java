package net.guerlab.sms.server.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.guerlab.sms.core.domain.VerifyInfo;
import net.guerlab.sms.core.exception.VerificationCodeIsNullError;
import net.guerlab.sms.core.exception.VerifyFailError;
import net.guerlab.sms.server.service.VerificationCodeService;

/**
 * 手机验证码
 *
 * @author guer
 */
@RestController
@RequestMapping("/sms/verificationCode")
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService service;

    /**
     * 获取验证码
     *
     * @param phone
     *            手机号码
     */
    @PostMapping("/{phone}")
    public void send(@PathVariable("phone") String phone) {
        service.send(phone);
    }

    /**
     * 获取验证码
     *
     * @param phone
     *            手机号码
     * @return 发送响应
     */
    @GetMapping(value = "/{phone}", produces = "application/json")
    public String get(@PathVariable("phone") String phone,
            @RequestParam("identificationCode") String identificationCode) {
        String code = service.find(phone, identificationCode);

        if (StringUtils.isBlank(code)) {
            throw new VerificationCodeIsNullError();
        }

        return code;
    }

    /**
     * 验证信息
     *
     * @param verifyInfo
     *            验证信息
     */
    @PostMapping
    public void verify(@RequestBody VerifyInfo verifyInfo) {
        if (!service.verify(verifyInfo.getPhone(), verifyInfo.getCode(), verifyInfo.getIdentificationCode())) {
            throw new VerifyFailError();
        }
    }
}
