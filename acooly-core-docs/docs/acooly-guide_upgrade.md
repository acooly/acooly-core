<!-- title: Acooly-V5  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-10-1 -->

Acooly-V5新版本升级指南
====

## 1 简介
如题，解决在大版本升级时的注意事项和相关的补充操作。

## 2 v4 To v5升级

### 2.1 配置参数

所有boot的配置参数，全部从驼峰模式切换为`-`分割。

### 2.2 数据库
* security组件在v5后，所有的数据表统一调整为小写，v4版本的工程sys_开头的数据表需要全部转换为小写。

```sql
rename table SYS_CONFIG to sys_config1;
rename table sys_config1 to sys_config;
rename table SYS_ORG to sys_org1;
rename table sys_org1 to sys_org;
rename table SYS_PORTALLET to sys_portallet1;
rename table sys_portallet1 to sys_portallet;
rename table SYS_ROLE to sys_role1;
rename table sys_role1 to sys_role;
rename table SYS_USER to sys_user1;
rename table sys_user1 to sys_user;
```

## 3 v5-changelogs

### acooly-common-facade-5.0.0-SNAPSHOT-20200120

* 重构Ids为IP作为节点标志方案，解决Did及相关的GID和OID在各环境（包括容器）可能重复的问题。目前精度支持秒级99999,调整Ids的getSequ实现，采用全方法lock+实例变量模式。

    ```
    getDid():20012010332401900001,length:20
    gid():G20012010332401900002,length:21
    gid(systemCode:S001):S00120012010332401900003,length:24
    gid(systemCode:S001,reserved:R12345678):S0011234567820012010332401900004,length:32
    oid:O20012010332401900005,length:21
    oid(systemCode:S001):S00120012010332401900006,length:24
    mid():5E2511744F6B150757489265,length:24
    ```

* 新增并发测试工具类(Tasks)，提供多线程准备就绪后，并发（同时）运行测试任务。