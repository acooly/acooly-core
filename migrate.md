## 迁移文档

### 1. 数据源

1. 配置数据源使用`acooly.ds`配置前缀
2. 开启sql日志使用`acooly.ds.slowSqlThreshold=0`

### 2. jpa

1. 新增实体扫描包`org.springframework.boot.autoconfigure.domain.EntityScan`