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
package net.guerlab.sms.chinamobile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.guerlab.sms.core.domain.NoticeData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class ChinaMobileSignTest {

    @Test
    public void sendFailTest() {
        ChinaMobileProperties properties = new ChinaMobileProperties();
        properties.setEcName("政企分公司测试");
        properties.setApId("demo0");
        properties.setSecretKey("123qwe");
        properties.setSign("4sEuJxDpC");
        properties.setTemplates(Collections.singletonMap("test", "38516fabae004eddbfa3ace1d4194696"));
        properties.setParamsOrders(Collections.singletonMap("test", Collections.singletonList("code")));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ChinaMobileSendHandler sendHandler = new ChinaMobileSendHandler(properties, null, objectMapper);

        NoticeData noticeData = new NoticeData();
        noticeData.setType("test");
        noticeData.setParams(Collections.singletonMap("code", "abcde"));

        Assert.assertFalse(sendHandler.send(noticeData, Collections.singletonList("13800138000")));
    }
}
