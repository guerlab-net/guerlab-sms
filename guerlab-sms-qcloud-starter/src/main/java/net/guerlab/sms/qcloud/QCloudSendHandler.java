package net.guerlab.sms.qcloud;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;
import net.guerlab.sms.core.utils.StringUtils;

import java.util.*;

/**
 * 腾讯云发送处理
 *
 * @author guer
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Slf4j
public class QCloudSendHandler implements SendHandler {

    private static final String DEFAULT_NATION_CODE = "86";

    private final QCloudProperties properties;

    private final SmsMultiSender sender;

    public QCloudSendHandler(QCloudProperties properties) {
        this.properties = properties;
        sender = new SmsMultiSender(properties.getAppId(), properties.getAppkey());
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        Integer templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        List<String> paramsOrder = properties.getParamsOrder(type);

        ArrayList<String> params = new ArrayList<>();

        if (!paramsOrder.isEmpty()) {
            Map<String, String> paramMap = noticeData.getParams();
            for (String paramName : paramsOrder) {
                String paramValue = paramMap.get(paramName);

                params.add(paramValue);
            }
        }

        Map<String, ArrayList<String>> phoneMap = new HashMap<>(phones.size());

        for (String phone : phones) {
            if (StringUtils.isBlank(phone)) {
                continue;
            }
            if (phone.startsWith("+")) {
                String[] values = phone.split(" ");

                if (values.length == 1) {
                    getList(phoneMap, DEFAULT_NATION_CODE).add(phone);
                } else {
                    String nationCode = values[0];
                    String phoneNumber = StringUtils.join(values, "", 1, values.length);

                    getList(phoneMap, nationCode).add(phoneNumber);
                }

            } else {
                getList(phoneMap, DEFAULT_NATION_CODE).add(phone);
            }
        }

        return phoneMap.entrySet().parallelStream()
                .allMatch(entry -> send0(templateId, params, entry.getKey(), entry.getValue()));
    }

    private Collection<String> getList(Map<String, ArrayList<String>> phoneMap, String nationCode) {
        return phoneMap.computeIfAbsent(nationCode, k -> new ArrayList<>());
    }

    private boolean send0(int templateId, ArrayList<String> params, String nationCode, ArrayList<String> phones) {
        try {
            SmsMultiSenderResult result = sender
                    .sendWithParam(nationCode, phones, templateId, params, properties.getSmsSign(), "", "");

            if (result.result == 0) {
                return true;
            }

            log.debug("send fail[code={}, errMsg={}]", result.result, result.errMsg);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        return false;
    }
}
