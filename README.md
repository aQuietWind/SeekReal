一个较为简单的java微服务型后端项目，用于学习以及技术提升。
# 🚀 UserApplication - Java 微服务后端项目

一个用于学习与技术提升的轻量级 Java 微服务后端项目，涵盖主流微服务技术栈，目前处于前后端联调阶段。

---

## 📚 技术栈

| 技术 | 用途 |
| :--- | :--- |
| **Spring Boot** | 项目基础框架 |
| **Spring Cloud Alibaba / Nacos** | 服务注册与配置中心 |
| **MySQL** | 关系型数据库存储 |
| **Redis** | 缓存与分布式锁 |
| **Elasticsearch (ES)** | 全文检索与日志分析 |
| **RabbitMQ** | 消息队列/异步解耦 |
| **MyBatis** | ORM 数据持久化 |
| **Feign** | 服务间远程调用 |

---

## ✨ 项目特点

- ✅ 基于主流微服务架构，模块化拆分清晰
- ✅ 集成 Nacos 实现服务发现与配置管理
- ✅ 引入 Redis 缓存，提升系统响应性能
- ✅ 使用 RabbitMQ 实现异步任务与解耦
- ✅ 接入 Elasticsearch 实现高效全文检索
- ✅ 已完成核心业务模块开发，支持基础联调

> ⚠️ 说明：项目目前处于前后端联调阶段，核心业务逻辑已实现，但可能存在未发现的 Bug 或业务漏洞，可作为学习参考与二次开发基础。

---

## 📁 项目结构（示例）

```text
UserApplication
├── user-common          # 公共模块（工具类、常量、异常）
├── user-service         # 核心业务服务模块
├── user-api             # Feign 接口模块
├── user-es              # Elasticsearch 检索模块
├── user-redis           # Redis 缓存模块
├── user-mq              # RabbitMQ 消息模块
└── sql/
    └── mysql&es建表语句.sql   # 数据库初始化脚本

 
🚀 快速启动
 
1. 环境要求
 
- JDK 21+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+
- Elasticsearch 9.3.2
- RabbitMQ 3.8+
- Nacos 2.x
 
2. 启动步骤
  
# 1. 克隆项目
git clone <你的项目地址>
cd UserApplication

# 2. 导入 MySQL 脚本
# 打开你的 MySQL 客户端，执行 quickStart.sql

# 3. 通过请求工具，如Postman，APIfox等进行es索引库建立（代码在esQuickStart中）

# 4. 修改配置文件
# 修改 application.yml 中的数据库、Redis、Nacos、ES、RabbitMQ 地址

# 5. 启动 Nacos、Redis、MySQL、RabbitMQ、Elasticsearch
# 6. 启动项目
mvn clean install
mvn spring-boot:run

 
📌 后续计划
 
完善单元测试
修复联调阶段发现的 Bug
补充接口文档（Swagger/OpenAPI）
加入限流、熔断、降级（Sentinel）
可能补充Seata的使用
工程化和简便化不够完善
接入日志收集与链路追踪
 
 











