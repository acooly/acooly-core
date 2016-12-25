## 迁移文档
### 1. 日志

不需要配置

### 1. 数据源

1. 配置数据源使用`acooly.ds`配置前缀
2. 开发者模式时打印sql

### 2. jpa

不需要配置

### 3. jsp

1. jsp文件路径为`src/main/resources/META-INF/resources/WEB-INF/jsp'
2. 修改include,例如`<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>`

### 4. freemarker

1. 支持从类路径中加载模板

### 5. 分布式session

1. 不需要配置
2. 使用redis，并且使用kryo序列化

### 6. cache

1. 依赖redis