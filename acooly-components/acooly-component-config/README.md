## 1. 组件介绍

此组件提供配置管理和读取的能力。读取值时首先读取本地缓存，然后读取redis，最后读取数据库。

## 2. 使用

### 2.1 读取配置

    com.acooly.module.config.Configs#getXXX  
       
### 2.2 运行时增加配置

        @Autowired
       private AppConfigManager configManager;
       
       AppConfig config=new AppConfig();
       configManager.create(config);