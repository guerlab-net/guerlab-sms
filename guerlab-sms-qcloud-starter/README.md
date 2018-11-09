# guerlab-sms-qcloud-starter

## maven配置

```
<dependency>
	<groupId>net.guerlab.sms</groupId>
	<artifactId>guerlab-sms-qcloud-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 安装教程

#### 1.引入jar包

```
<dependency>
    <groupId>net.guerlab.sms</groupId>
    <artifactId>guerlab-sms-qcloud-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2.bootstrap.yml增加配置项 sms.*

```
sms:
  qcloud:
    app-id: ##短信应用SDK AppID
    app-key: ##短信应用SDK AppKey
    sms-sign: ##短信签名
    templates: ##短信模板配置，key为业务层的net.guerlab.sms.core.domain.NoticeData中type的值，value为阿里云中生成的短信模板ID
      test: 123 ##test业务调用短信模板123
    params-orders: ##指定短信模板的参数顺序
      test: ##test业务的短信模板参数将按照下列顺序转换为腾讯云短信应用sdk所需要的数组参数
        - a
        - b
        - c