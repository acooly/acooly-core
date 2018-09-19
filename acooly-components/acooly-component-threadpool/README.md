<!-- title: 线程池组件 -->
<!-- type: infrastructure -->
<!-- author: qiubo -->
## 1. 组件介绍

   定制线程池，增加线程异常包裹和`MDC`传递
   
## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-threadpool</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

### 2.1 获取线程池

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    