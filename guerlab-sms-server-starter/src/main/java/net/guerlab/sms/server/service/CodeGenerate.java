package net.guerlab.sms.server.service;

/**
 * 验证码生成
 *
 * @author guer
 */
@FunctionalInterface
public interface CodeGenerate {

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    String generate();
}
