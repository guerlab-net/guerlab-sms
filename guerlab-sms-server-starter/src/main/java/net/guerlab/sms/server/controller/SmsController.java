package net.guerlab.sms.server.controller;

import net.guerlab.sms.core.domain.NoticeInfo;
import net.guerlab.sms.core.domain.VerifyInfo;
import net.guerlab.sms.core.exception.VerificationCodeIsNullException;
import net.guerlab.sms.core.exception.VerifyFailException;
import net.guerlab.sms.server.service.NoticeService;
import net.guerlab.sms.server.service.VerificationCodeService;
import net.guerlab.web.result.Result;
import net.guerlab.web.result.Succeed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信Controller
 *
 * @author guer
 */
@RestController
public class SmsController {

    /**
     * 手机验证码服务
     */
    private VerificationCodeService verificationCodeService;

    /**
     * 短信通知服务
     */
    private NoticeService noticeService;

    @Autowired
    public void setVerificationCodeService(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 获取验证码
     *
     * @param phone
     *            手机号码
     */
    public void sendVerificationCode(@PathVariable("phone") String phone) {
        verificationCodeService.send(phone);
    }

    /**
     * 获取验证码
     *
     * @param phone
     *            手机号码
     * @return 发送响应
     */
    public Result<String> getVerificationCode(@PathVariable("phone") String phone,
            @RequestParam(value = "identificationCode", required = false, defaultValue = "") String identificationCode) {
        String code = verificationCodeService.find(phone, identificationCode);

        if (StringUtils.isBlank(code)) {
            throw new VerificationCodeIsNullException();
        }

        return new Succeed<>(Succeed.MSG, code);
    }

    /**
     * 验证信息
     *
     * @param verifyInfo
     *            验证信息
     */
    public void verifyVerificationCode(@RequestBody VerifyInfo verifyInfo) {
        if (!verificationCodeService.verify(verifyInfo.getPhone(), verifyInfo.getCode(),
                verifyInfo.getIdentificationCode())) {
            throw new VerifyFailException();
        }
    }

    /**
     * 发送通知
     *
     * @param info
     *            通知内容
     */
    public void sendNotice(@RequestBody NoticeInfo info) {
        noticeService.send(info.getNoticeData(), info.getPhones());
    }
}
