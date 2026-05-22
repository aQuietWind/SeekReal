<h1 align="center"> 🚀 SeekReal - Java 微服务后端项目 </h1>

<p align="center">一个用于学习与技术提升的轻量级 Java 微服务后端项目，涵盖主流微服务技术栈。
</p>
<p align="center">
本篇基本涵盖了大部分的实用技术栈<small>（至少该有的基本都有了）</small>，并不仅仅只是停留于使用层面，更多的还有对中间件和数据库性能
与优化之间的考虑。
</p>
<p align="center">
<b>如果这个项目对您的后端学习有帮助，请考虑给它一个 ⭐ Star呗～</b>
</p>

---

## 📚 技术栈

| 技术                |     版本     | 说明          | 必要性                                              |
|-------------------|:----------:|-------------|--------------------------------------------------|
| Spring Boot       |   3.5.14   | Web服务快速构建框架 | 必须                                          |
| MyBatis           |   3.0.5    | 数据库连接框架     | 必须                                          |                                     
| Caffeine          |   3.1.8    | JVM本地缓存支持   | 少量使用                                             |
| JWT               |   0.13.0   | 帐号验证        | 几乎不可消除                                             |
| Redis             |   8.6.1    | 分布式缓存支持     | 许多场景使用，且作用难以替换                                   |
| MySQL             |   8.0.46   | 数据库服务       | 必须                                         |  
| Elasticsearch     |   9.3.2    | 搜索引擎服务      | 必须                                          |
| RabbitMQ          |   3.13.7   | 消息中间件       | 几乎不可消除                                           |                               
| Nacos             |   2.0.0    | 注册中心        | 必须                                          |
| Fegin             | 2025.0.2 | 模块间通信       | 少量使用                                             |
| Gateway           | 2025.0.2 | 网关层核心       | 几乎不可消除                                          |
| Sentinel          |    2.4     | 流量控制工具      | 可有可无，但系统有一定依赖性 |
| Springdoc-openapi |   2.8.15   | 接口文档自动生成    | 可有可无，不需要了直接去除依赖就行                                |
| Docker            |     -      | 快速容器部署工具    | 可有可无,自己用来部署                                      |


---

## ✨ 项目特点

- 有良好的可学习基底，每个方法每行代码都尽量做到有充足的注释进行解释，如果注释太少看不懂，我后续也会回来继续补充的
- 有可行的工程化基础，对于redis的key名尽量使用一个专门的类来管理</small>（虽然不够全面推行）</small>
- 除了必须的以外，可以确保几乎没有别的依赖了，大部分也都是自己实现的，
- 有很多额外的扩展知识，实用但是从来没有学过的</small>（说实话很多我自己也是第一次才学到）</small>

> ⚠️ 说明：项目目前还未有完整的前端呈现，所以可能存在对于前端人员不友好的问题，望原谅<small>（因为我实在不想写前端qwq）</small>，
> 虽然核心业务逻辑已实现，但不排除仍然可能会遇见一些麻烦的问题，比如一些未发现的 Bug 或业务漏洞，不过应该还可作为学习参考
> 与二次开发基础。

---

## 📁 项目结构
```
SeekReal
├── GateWay          # 网关模块，进行请求过滤
├── User             # 用户模块，进行对用户的相关操作
├── KnowAsk          # 提问与文章模块
├── Comment          # 评论模块
├── Appreciation     # 点赞收藏模块
├── Social           # 社交模块，主要是公告，通知，关注等功能
├── Pojo             # 实体类模块，专门存放实体类
└── Util             # 工具模块，专门存放统一使用的工具类
```
 
## 🚀 快速启动
 
1. 环境要求<small>（低一点也没事）</small>
 
- JDK 21+
- Maven 3.6+
- MySQL 8.0+
- Redis 8.0+
- Elasticsearch 9.3.2  <small>（硬性要求）</small>
- RabbitMQ 3.13.7+
- Nacos 2.x+
 
启动步骤
  
 1. 克隆项目
git clone https://github.com/aQuietWind/SeekReal

 2. 导入 MySQL 脚本
 打开你的 MySQL 客户端，执行 quickStart.sql

 3. 通过请求工具，如Postman，APIfox等，进行es索引库建立<small>（代码在esQuickStart中）</small>

 4. 修改配置文件
 修改 application.yml 中的数据库、Redis、Nacos、ES、RabbitMQ 地址和密码

 5. 启动 Nacos、Redis、MySQL、RabbitMQ、Elasticsearch

 6. 启动项目
mvn clean install
mvn spring-boot:run

## 接口文档
将GateWay,KnowAsk,Social,User,Comment,Appreciation模块启动后，进入 http://localhost:80xx/swagger/index.html 即可
<small>（根据模块的启动端口号填写即可）</small>
如 <img src=./Image/showSwagger.png>
少量有些接口的参数是可选的，可能会对各位造成麻烦,至于存储照片的前端获取，我则到时候会放到nginx做静态托管


## 📌 后续计划
 
1. 完善单元测试
2. 修复未来将会修复联调阶段（遥遥无期）发现的 Bug
3. 该项目可能存在一些短板问题，以及性能问题，还望谅解
4. 可能补充Seata的使用
5. 对于MQ队列和交换机的命名没有用Enum类进行统一管理，对后续维护和更新可能有较大麻烦
6. 存在注释空缺，可能会对各位参考造成一定麻烦，日后也会进行相应的优化
7. 接入日志收集与链路追踪
8. 性能上还有进步空间，没有大量使用Caffine缓存和Redis缓存进行性能优化<small>（尤其是对于网关层ip封锁那个过滤器，性能更是差,也许可以改进为 定时任务+jvm缓存）</small>
9. 部分功能不齐，比如说关注之后的粉丝消息推送，又如文章和提问打标签等等问题</small>（相信一定有哪位能人可以优化和做的比我更好）</small>

## 提示点
1. 我的yaml配置文档中没有redis的配置,而是放到了Config中手动配置，这是因为我无法使用lettuce进行redis连接，
只能用jedis，所以你们如果要更改redis的配置地点的话，就需要到各个模块的RedisConfig中更改
---











