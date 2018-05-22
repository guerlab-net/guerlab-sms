package net.guerlab.sms.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guerlab.sms.core.domain.NoticeInfo;
import net.guerlab.sms.server.service.NoticeService;

/**
 * 短信通知
 *
 * @author guer
 */
@RestController
@RequestMapping("/sms/notice")
public class NoticeController {

    @Autowired
    private NoticeService service;

    /**
     * 发送通知
     *
     * @param info
     *            通知内容
     */
    @PutMapping
    public void notice(@RequestBody NoticeInfo info) {
        service.send(info.getNoticeData(), info.getPhones());
    }
}
