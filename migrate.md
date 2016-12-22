## 迁移文档
### 1. 日志

不需要配置

### 1. 数据源

1. 配置数据源使用`acooly.ds`配置前缀
2. 开启sql日志使用`acooly.ds.slowSqlThreshold=0`

### 2. jpa

不需要配置

### 3. jsp

1. jsp文件路径为`src/main/resources/META-INF/resources/WEB-INF/jsp'
2. 修改include,例如`<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp"%>`

### 4. freemarker

1. 支持从类路径中加载模板
2. 建议不要使用`spring.freemarker.expose-session-attributes=true`,由于原功能需要，目前开启

### 5. 分布式session

1. 不需要配置