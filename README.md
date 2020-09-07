# guerlab-sms

基于Spring boot的短信服务支持，通过引用不同的Starter启用不同的短信通道支持

![](https://img.shields.io/maven-central/v/net.guerlab.sms/guerlab-sms-server-starter.svg)
![](https://travis-ci.org/guerlab-net-sms/guerlab-sms-core.svg?branch=master)
![](https://img.shields.io/badge/LICENSE-LGPL--3.0-brightgreen.svg)

## maven配置

```
<dependency>
	<groupId>net.guerlab.sms</groupId>
	<artifactId>guerlab-sms-server-starter</artifactId>
</dependency>
```

## 子项目列表

|子项目|说明|
|:--|--|
|guerlab-sms-core|核心包|
|guerlab-sms-server-starter|短信服务实现|
|guerlab-sms-redis-repository-starter|基于redis的repository实现|

## 支持通道

- [x] 阿里云  guerlab-sms-aliyun-starter
- [x] 腾讯云  guerlab-sms-qcloud-starter
- [x] 华为云  guerlab-sms-huaweicloud-starter
- [x] 京东云  guerlab-sms-jdcloud-starter
- [x] 七牛云  guerlab-sms-qiniu-starter
- [X] 网易云信  guerlab-sms-netease-starter
- [X] 云片网  guerlab-sms-yunpian-starter
- [X] 又拍云  guerlab-sms-upyun-starter
- [X] 百度云 guerlab-sms-baiducloud-starter

## wiki

- [Gitee](https://gitee.com/guerlab_net/guerlab-sms/wikis/pages)

## changelog

- [Gitee](https://gitee.com/guerlab_net/guerlab-sms/wikis/pages)

## demo

- [Gitee](https://gitee.com/guerlab_net/guerlab-sms-demo)
