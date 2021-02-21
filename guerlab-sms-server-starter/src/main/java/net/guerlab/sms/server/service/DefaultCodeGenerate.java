package net.guerlab.sms.server.service;

import net.guerlab.sms.server.properties.VerificationCodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 默认验证码生成
 *
 * @author guer
 */
@Component
public class DefaultCodeGenerate implements CodeGenerate {

    private VerificationCodeProperties properties;

    @Autowired
    public void setProperties(VerificationCodeProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate() {
        int codeLength = properties.getCodeLength();

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(codeLength);
        format.setMinimumIntegerDigits(codeLength);

        return format.format(ThreadLocalRandom.current().nextInt((int) Math.pow(10, codeLength)));
    }

}
