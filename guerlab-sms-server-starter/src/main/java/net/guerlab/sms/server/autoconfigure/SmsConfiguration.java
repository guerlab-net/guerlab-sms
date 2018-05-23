package net.guerlab.sms.server.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.guerlab.sms.server.properties.SmsProperties;
import net.guerlab.sms.server.repository.IVerificationCodeRepository;
import net.guerlab.sms.server.repository.VerificationCodeMemoryRepository;

/**
 * 短信服务配置
 *
 * @author guer
 *
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ComponentScan({
        "net.guerlab.sms.server.repository", "net.guerlab.sms.server.service"
})
@Import(WebConfiguration.class)
public class SmsConfiguration {

    @Bean
    @ConditionalOnMissingBean(IVerificationCodeRepository.class)
    public IVerificationCodeRepository verificationCodeMemoryRepository() {
        return new VerificationCodeMemoryRepository();
    }
}
