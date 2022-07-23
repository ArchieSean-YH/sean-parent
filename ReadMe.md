# 基于springsecurity的新版本认证服务器，实现基本RBAC功能

## 技术栈

* springboot2.7.x
* springcloud 2021.0.1
* springcloudAlibaba 2021.0.1.0
* mysql 5.7
* redisson

## 项目结构划分

* 按照业务逻辑一体，pojo和feign单独分离的方式进行使用。【方便其余系统使用pojo和feign】

## 交互方式

* 计划各资源服务之间使用JWT进行交互，各业务系统使用RSA公钥进行解密，获取当前用户相关信息
* 后续扩展功能，将放置在third模块当中
