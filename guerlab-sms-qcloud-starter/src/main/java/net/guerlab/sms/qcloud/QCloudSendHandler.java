package net.guerlab.sms.qcloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qcloudsms.SmsMultiSender;

import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

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
    public void send(NoticeData noticeData, Collection<String> phones) {
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

        phoneMap.entrySet().parallelStream().forEach(entry -> send0(noticeData, entry.getKey(), entry.getValue()));
    }

    private Collection<String> getList(Map<String, ArrayList<String>> phoneMap, String nationCode) {
        ArrayList<String> list = phoneMap.get(nationCode);

        if (list == null) {
            list = new ArrayList<>();

            phoneMap.put(nationCode, list);
        }

        return list;
    }

    private void send0(NoticeData noticeData, String nationCode, ArrayList<String> phones) {
        int templateId = properties.getTemplates(noticeData.getType());

        ArrayList<String> params = new ArrayList<>(new LinkedHashMap<>(noticeData.getParams()).values());

        try {
            sender.sendWithParam(nationCode, phones, templateId, params, properties.getSmsSign(), "", "");
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
        }
    }
}