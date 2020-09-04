package net.guerlab.sms.qiniu;

import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import net.guerlab.sms.core.domain.NoticeData;
import net.guerlab.sms.core.handler.SendHandler;

import java.util.Collection;

/**
 * 七牛云发送处理
 *
 * @author guer
 */
@Slf4j
public class QiNiuSendHandler implements SendHandler {

    private final QiNiuProperties properties;

    private final SmsManager smsManager;

    public QiNiuSendHandler(QiNiuProperties properties) {
        this.properties = properties;
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        smsManager = new SmsManager(auth);
    }

    @Override
    public boolean send(NoticeData noticeData, Collection<String> phones) {
        String type = noticeData.getType();

        String templateId = properties.getTemplates(type);

        if (templateId == null) {
            log.debug("templateId invalid");
            return false;
        }

        try {
            Response response = smsManager
                    .sendMessage(templateId, phones.toArray(new String[] {}), noticeData.getParams());

            if (!response.isOK()) {
                log.debug("send fail, error: {}", response.error);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage(), e);
        }

        return false;
    }
}
