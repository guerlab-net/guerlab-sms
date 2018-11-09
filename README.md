# guerlab-sms

## 项目介绍
基于Spring boot的短信服务支持，通过引用不同的Starter启用不同的短信通道支持

## maven配置

```
<dependency>
	<groupId>net.guerlab.sms</groupId>
	<artifactId>guerlab-sms-server-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 安装教程

#### 1.引入jar包

```
<dependency>
    <groupId>net.guerlab.sms</groupId>
    <artifactId>guerlab-sms-server-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2.bootstrap.yml增加配置项 sms.*

```
sms:
  reg: ##手机号码正则表达式，为空则不做验证
  verification-code:
    code-length: 6 ##验证码长度
    delete-by-verify-fail: false ##为true则验证失败后删除验证码
    delete-by-verify-succeed: true ##为true则验证成功后删除验证码
    expiration-time:  ##验证码有效期，单位秒
    identification-code-length: 3 ##识别码长度
    use-identification-code: false ##是否启用识别码
  web:
    enbale: true ##启用web端点
    base-path: /sms ##基础路径,默认为/sms,实现类为net.guerlab.sms.server.controller.SmsController
```

#### 3.启动方法增加注解项@EnableSmsServer

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.guerlab.sms.server.annotation.EnableSmsServer;

@SpringBootApplication
@EnableSmsServer
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}

```

#### 4.定制验证码的储存位置

实现net.guerlab.sms.server.repository.IVerificationCodeRepository接口即可

可参考[guerlab-sms-redis-repository-starter](./guerlab-sms-redis-repository-starter)实现

#### 5.定制具体的业务实现类

阿里云短信使用方式直接添加依赖
```
<dependency>
    <groupId>net.guerlab.sms</groupId>
    <artifactId>guerlab-sms-aliyun-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

腾讯云短信使用方式直接添加依赖
```
<dependency>
    <groupId>net.guerlab.sms</groupId>
    <artifactId>guerlab-sms-qcloud-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

其他运营商实现方式请自行实现net.guerlab.sms.core.handler.SendHandler

#### 6.发送验证码

#### 6.1 注入VerificationCodeService  (net.guerlab.sms.server.service.VerificationCodeService)

#### 6.2 发送验证码

调用verificationCodeService.send(phone)方法进行验证码发送

#### 6.3 验证码验证

调用verificationCodeService.verify(phone, code, identificationCode)进行验证，其中code为验证码，identificationCode为识别码，识别码非必填

#### 7.发送通知

#### 7.1 注入NoticeService  (net.guerlab.sms.server.service.NoticeService)

#### 7.2 发送通知

调用noticeService.send(noticeData, phones)进行通知发送，noticeData为net.guerlab.sms.core.domain.NoticeData实例，phones为手机号码列表