# guerlab-sms

## 项目介绍
基于Spring boot的短信服务支持，通过引用不同的Starter启用不同的短信通道支持

## maven配置

```
<dependency>
	<groupId>net.guerlab</groupId>
	<artifactId>guerlab-sms-server-starter</artifactId>
	<version>0.0.2-SNAPSHOT</version>
</dependency>
<repositories>
	<repository>
		<id>sonatype-nexus-snapshots</id>
		<url>https://oss.sonatype.org/content/repositories/snapshots/</url>
		<releases>
			<enabled>false</enabled>
		</releases>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</repository>
</repositories>
```

## 安装教程

#### 1.引入jar包

```
    <dependencies>
        <dependency>
            <groupId>net.guerlab</groupId>
            <artifactId>guerlab-sms-server-starter</artifactId>
            <version>0.0.2-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

#### 2.bootstrap.yml增加配置项 sms.*

```
sms:
  enable-web: true ##启用web端点
  reg: ##手机号码正则表达式，为空则不做验证
  verification-code:
    code-length: 6 ##验证码长度
    delete-by-verify-fail: false ##为true则验证失败后删除验证码
    delete-by-verify-succeed: true ##为true则验证成功后删除验证码
    expiration-time:  ##验证码有效期，单位秒
    identification-code-length: 3 ##识别码长度
    use-identification-code: false ##是否启用识别码
```

#### 3.启动方法增加注解项@EnableSmsServer

```
package net.guerlab.sms.test;

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

#### 5.测试系统尝试功能

#### 5.1 发送验证码

POST请求访问http://localhost:8080/sms/verificationCode/{电话号码} 进行验证码发送

#### 5.2 获取验证码信息

GET请求访问http://localhost:8080/sms/verificationCode/{电话号码} 进行验证码信息获取

#### 5.2 验证码验证

POST请求发送请求提访问http://localhost:8080/sms/verificationCode 进行验证码信息验证

请求体
```
{"phone":"电话号码","code":"验证码","identificationCode":"识别码"}
```

未启动识别码请求体中可不传识别码