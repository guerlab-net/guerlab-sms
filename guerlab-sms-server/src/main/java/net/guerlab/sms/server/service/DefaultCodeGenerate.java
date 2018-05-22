package net.guerlab.sms.server.service;

import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.guerlab.sms.server.properties.SmsProperties;

/**
 * 默认验证码生成
 * 
 * @author guer
 *
 */
@Component
public class DefaultCodeGenerate implements ICodeGenerate {

    @Autowired
    private SmsProperties properties;

    @Override
    public String generate() {
        int codeLength = properties.getVerificationCode().getCodeLength();

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(codeLength);
        format.setMinimumIntegerDigits(codeLength);

        return format.format(ThreadLocalRandom.current().nextInt((int) Math.pow(10, codeLength)));
    }

}
