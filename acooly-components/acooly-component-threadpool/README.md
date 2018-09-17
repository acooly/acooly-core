---
title: 线程池组件
type: infrastructure
author: qiubo
---
## 1. 组件介绍

   定制线程池，增加线程异常包裹和`MDC`传递
   
## 2. 使用说明

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    