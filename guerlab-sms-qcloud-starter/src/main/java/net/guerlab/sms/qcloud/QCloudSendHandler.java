package net.guerlab.sms.qcloud;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import net.guerlab.commons.collection.CollectionUtil;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QCloudSendHandler implements SendHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(QCloudSendHandler.class);

    private static final String DEFAULT_NATION_CODE = "86";

    private QCloudProperties properties;

    private SmsMultiSender sender;

    public QCloudSendHandler(QCloudProperties properties) {
        this.properties = properties;
        initClient();
    }

    private void initClient() {
        int appid = properties.getAppId();
        String appkey = properties.getAppkey();

        sender = new SmsMultiSender(appid, appkey);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        Integer templateId = properties.getTemplates(type);

        if (templateId == null) {
            LOGGER.debug("templateId invalid");
            return false;
        }

        List<String> paramsOrder = properties.getParamsOrder(type);

        ArrayList<String> params = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(paramsOrder)) {
            Map<String, String> paramMap = noticeData.getParams();
            for (String paramName : paramsOrder) {
                String paramValue = paramMap.get(paramName);

                params.add(paramValue);
            }
        }

        Map<String, ArrayList<String>> phoneMap = new HashMap<>();

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
                .map(entry -> send0(templateId, params, entry.getKey(), entry.getValue())).filter(v -> !v).count() == 0;
    }

    private Collection<String> getList(Map<String, ArrayList<String>> phoneMap, String nationCode) {
        ArrayList<String> list = phoneMap.get(nationCode);

        if (list == null) {
            list = new ArrayList<>();

            phoneMap.put(nationCode, list);
        }

        return list;
    }

    private boolean send0(int templateId, ArrayList<String> params, String nationCode, ArrayList<String> phones) {
        try {
            SmsMultiSenderResult result = sender
                    .sendWithParam(nationCode, phones, templateId, params, properties.getSmsSign(), "", "");

            if (result.result == 0) {
                return true;
            }

            LOGGER.debug("send fail[code={}, errMsg={}]", result.result, result.errMsg);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
        }

        return false;
    }
}