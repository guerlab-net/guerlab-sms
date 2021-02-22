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
