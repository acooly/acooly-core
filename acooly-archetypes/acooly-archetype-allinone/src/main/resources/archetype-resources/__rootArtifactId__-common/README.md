公共模块
====

## 注意

该模块是只依赖acooly-common,不依赖acooly-core，因为有可能该模块会对外发布。如果存在对acooly-core或spring容器的依赖，请新增模块作为基础依赖模块或使用:`xxxx-platform-common`

## 定位
common模块是全站公用的基础结构和工具，底层依赖，尽量减少对外部依赖，在全工程项目中公用（包含platfrom业务模块，facade微服务接口，openapi服务接口等）。比如：

* 公共枚举（例如: `XxxTypeEnum`）
* 公共异常
* 公共工具
* 公共DTO
* 公共常量等
* 项目配置参数

