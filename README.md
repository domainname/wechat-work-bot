# 企业微信 Webhook 机器人

[![Build Status](https://travis-ci.org/domainname/wechat-work-bot.svg?branch=master)](https://travis-ci.org/domainname/wechat-work-bot)

企业微信 2.8.7 版本上线了“[群机器人](https://work.weixin.qq.com/help?person_id=1&doc_id=13376)”功能，提供 Webhook 接口，这样一来，企业微信也拥有了类似 Slack 的消息集成能力。wechat-work-bot 用于将其他应用（例如 GitLab）的消息通过 Webhook 推送至企业微信群中。

目前支持的应用：

- GitLab。支持的事件如下：

  - Push 事件
  - Issue 事件
  - Merge Request 事件
  - Note 事件（回复 merge request、issue、commit、snippet）
  - Pipeline 事件

## 示例



## 如何使用

### GitLab

进入 GitLab 项目的`Settings` ->`Integrations`，URL一栏填写`http://<服务器域名或IP>/gitlab/webhook/<key>`，例如：

```
http://localhost:8080/gitlab/webhook/1234abcd-1234-abcd-1234-abcd1234
```

