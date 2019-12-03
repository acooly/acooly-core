<!-- title: 代码生成  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-10-1 -->

acooly版本升级指南
====

## 1 简介
如题，解决在大版本升级时的注意事项和相关的补充操作。

## 2 v4 To v5升级

### 数据库
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
