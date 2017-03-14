通用抽奖组件
====

# 简介

通用抽奖组件提供了常规抽奖活动的逻辑封装和配置，主要特性包括：

* 活动的定义，支持最大人数，单人最大次数控制，同时提供按奖项控制抽奖次数的开关。
* 奖项的定义，支持多种类型的奖项，如果是现金，则支持面值定义；
* 支持按各种时间维度（无限制，按天，月等）控制奖项的最大出奖数量。
* 支持奖项的权重配置（中奖率）和是否保存中奖纪录的开关（如果奖项为未中奖，则一般设置为关闭）

# 集成

目标项目集成（acooly组件通用集成方案，pom引用）抽奖组件能力后，组件会自动创建数据库和配置BOSS管理权限。然后可以通过后台boss对抽奖活动进行管理。

> 默认情况下：初始化数据中已内置了一个完整的抽奖活动数据，用于测试。

## 管理

组件采用全后台boss管理。请登录boss查看。

主要包括：

* 抽奖活动管理：定义抽奖活动和奖项，设置抽奖参数和奖项参数。
* 抽奖白名单：定义必中的抽奖白名单。
* 抽奖用户次数：管理用户的单个次数
* 中奖列表：抽奖的实时中奖列表查询

## 接口


提供的核心对外能力主要通过：com.acooly.module.lottery.facade.LotteryFacade提供统一接口。

```java
public interface LotteryFacade {

    /**
     * 抽奖
     */
    LotteryResult lottery(LotteryOrder order);

    /**
     * 增加用户抽奖次数
     */
    ResultBase addLotteryCount(LotteryCountOrder order);

    /**
     * 查询用户的可抽奖次数
     */
    LotteryCountResult getLotteryCount(LotteryCountOrder order);

}
```

## OpenApi集成方案

如果采用前后端平台分离，网站平台需要开发抽奖的portal和说明，然后通过OpenAPI调用抽奖功能，同时在业务中根据业务需求为用户增加抽奖次数。


> OpenAPI接口可以在目标工程根据需求开发，实现采用调用LotteryFacade实现。

## Demo

组件默认提供了集成的Demo，具体类为：com.acooly.module.lottery.portal.LotteryDemoPortalController

主要提供了直接通过controler访问的demo，主要包括以下功能：

* 抽奖，访问地址为：/portal/lottery/demo/lottery.html
* 增加次数, 访问地址: /portal/lottery/demo/addCount.html?count=10
* 查询次数, 访问地址: /portal/lottery/demo/getCount.html

# 版本

## 20170315

### 特性说明

新增特性：**支持抽奖中奖时，发布事件**。

在抽奖活动的管理模块新增发布事件的开关，如果打开，则表示每次抽奖后都会发布事件。事件为：com.acooly.module.lottery.event.LotteryEvent。请参考acooly-component-event组件按需开发EventHandler进行处理。

该特性主要用于在抽奖完成后对中奖的奖项进行扩展业务处理。包括以下几种情况：

1. 如果中奖的是现金：调用对用的逻辑发放awardValue对应值的现金。
2. 如果是虚拟产品：如积分或卡券，根据配置的awardValue发放对应的积分和卡券
3. 如果是实物商品：根据配置的awardValue的商品编号进行处理，或通过通用的后台中奖列表进行处理。

>特别的：如果在这里处理自动发奖，请注入：lotteryWinnerService服务，变更发放的记录状态。

处理事件Demo

```java
/**
 * @author acooly
 */
@EventHandler
public class LotteryEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LotteryEventHandler.class);

    /**
     * 抽奖事件处理（异步）
     *
     * 1、如果中奖的是现金：调用对用的逻辑发放awardValue对应值的现金。
     * 2、如果是虚拟产品：如积分或卡券，根据配置的awardValue发放对应的积分和卡券
     * 3、如果是实物商品：根据配置的awardValue的商品编号进行处理，或通过通用的后台中奖列表进行处理。
     *
     * 如果在这里处理自动发奖，请注入：lotteryWinnerService服务，变更发放的记录状态。
     *
     * @param lotteryEvent
     */
    @Handler(delivery = Invoke.Asynchronously)
    public void handleEventAsync(LotteryEvent lotteryEvent) {
        logger.info("中奖事件处理：lotteryEvent：{}", lotteryEvent);
    }

}
```


### 升级说明
如果你原已集成了lottery组件，你可以选择删除集成数据（删除权限数据和所有的lottery表）重新集成（推荐方式），或者更新下面的SQL。

```sql
ALTER TABLE `lottery_award` 
CHANGE COLUMN `award_amount` `award_value` BIGINT(20) NULL DEFAULT NULL COMMENT '奖项值' ;
  
ALTER TABLE `lottery` 
ADD COLUMN `publish_event` VARCHAR(16) NULL COMMENT '发布事件' AFTER `user_counter`;
```





