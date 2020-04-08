<!-- title: Acooly工具库  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2020-1-1 -->

Acooly工具库
====

## 1 简介
集中介绍Acooly提供的常用工具库的特性和使用。

## 2 常用工具

### 2.1 分布式唯一ID：Ids

提供分布式不重复业务ID生成工具。

默认长度：20
组成结构：时间戳(12)+IP后两段(4)+动态段(可选，大于20的长度生效，>24:加入PID)+顺序数(4,0000~9999，最大秒级10000)

|段			|长度			|必选				|备注
|---------|-------------|---------------|-----------
|时间戳    |12				|是					|yyMMddHHmmss
|IP			|4				|是					|IP的后两段（掩码16）的Hex格式
|PID		|4				|否					|进程ID，长度大于24时有效
|随机数		|n				|否					| 20 < size < 24或 size > 24 
|顺序数		|4				|是					| 0000-9999的顺序数


实例如下：

```java
// 默认
Ids.did():        200127004631016E0001, length:20
// 传入size
Ids.did(24):      200127004631016E2B6C0002, length:24
// 全站统一跟踪唯一请求ID
Ids.gid():        G200127004631016E0003, length:21
Ids.gid(S001):    S001200127004631016E0004, length:24
Ids.gid(S001,...):S00112345678200127004631016E0005, length:32
// 内部系统订单号
Ids.oid():        O200127004631016E0006, length:  21
Ids.oid(S001):    S001200127004631016E0007, length:24
// MongodbId
Ids.mid():        5E2DC2674F6B152B6CBB120C, length:24
```
### 2.2 时间日期：Dates

线程安全的Date工具类，兼容JDK1.7及以上。提供常用的日期和时间相关的工具，包括：格式化，解析和运算等工具。


