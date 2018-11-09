# guerlab-sms-aliyun-starter

## maven配置

```
<dependency>
	<groupId>net.guerlab.sms</groupId>
	<artifactId>guerlab-sms-aliyun-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 安装教程

#### 1.引入jar包

```
<dependency>
    <groupId>net.guerlab.sms</groupId>
    <artifactId>guerlab-sms-aliyun-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2.bootstrap.yml增加配置项 sms.*

```
sms:
  aliyun:
    endpoint: cn-hangzhou ## 默认cn-hangzhou
    access-key-id: ##accessKeyId
    access-key-secret: ##accessKeySecret
    sign-name: ##短信签名
    templates: ##短信模板配置，key为业务层的net.guerlab.sms.core.domain.NoticeData中type的值，value为阿里云中生成的短信模板ID
      test: 123 ##test业务调用短信模板123