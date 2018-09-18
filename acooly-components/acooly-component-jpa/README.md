<!-- title: JPA组件  -->
<!-- type: infrastructure -->
<!-- author: qiubo -->

## 1. 组件介绍

此组件提供jpa相关能力

1. 提供`EntityJpaDao`，实现`EntityDao`能力
2. 自动注册`Money`类型

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-jpa</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。