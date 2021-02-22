/*
 * Copyright 2018-2021 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.guerlab.sms.server.repository;

import net.guerlab.sms.server.entity.VerificationCode;

/**
 * 验证码储存接口
 *
 * @author guer
 */
public interface VerificationCodeRepository {

    /**
     * 根据手机号码查询验证码
     *
     * @param phone
     *         手机号码
     * @param identificationCode
     *         识别码
     * @return 验证码
     */
    VerificationCode findOne(String phone, String identificationCode);

    /**
     * 保存验证码
     *
     * @param verificationCode
     *            验证码
     */
    void save(VerificationCode verificationCode);

    /**
     * 删除验证码
     *
     * @param phone
     *            手机号码
     * @param identificationCode
     *            识别码
     */
    void delete(String phone, String identificationCode);

}
